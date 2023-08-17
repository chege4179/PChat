/*
 * Copyright 2023 PChat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.pchat.presentation.ui.screens.dashboard.chat.chat_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.peterchege.pchat.core.util.DataResult
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.data.OfflineFirstMessageRepository
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.util.SocketHandler.mSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject


sealed interface ChatsScreenUiState {
    object Loading : ChatsScreenUiState

    data class Success(
        val chats: List<Message>,
        val receiver:NetworkUser,
        val authUser:NetworkUser,
    ) : ChatsScreenUiState

    data class Error(val message: String) : ChatsScreenUiState
}


@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val offlineFirstChatRepository: OfflineFirstChatRepository,
    private val offlineFirstMessageRepository: OfflineFirstMessageRepository,
    private val authRepository: AuthRepository,
    private val json:Json,

    ) : ViewModel() {
    val receiverId =  savedStateHandle.get<String>("receiverId") ?: ""
    val senderId = savedStateHandle.get<String>("senderId") ?: ""


    val uiState = combine(
        authRepository.getAuthUser(),
        offlineFirstMessageRepository.getChatMessagesBetween2Users(senderId = senderId, receiverId = receiverId)
    ){ authUser ,messages ->
        val receiverResult = offlineFirstChatRepository.getUserById(receiverId)
        when(receiverResult){
            is DataResult.Success -> {
                if (receiverResult.data != null){
                    ChatsScreenUiState.Success(
                        authUser = authUser!!,
                        receiver = receiverResult.data,
                        chats = messages.map { it.toExternalModel() }
                    )
                }else{
                    ChatsScreenUiState.Error(message = "Receiver Info not found")
                }
            }
            is DataResult.Error -> {
                ChatsScreenUiState.Error(message = "Receiver Info not found")
            }
        }
    }
        .onStart { ChatsScreenUiState.Loading }
        .catch { ChatsScreenUiState.Error(message = "An unexpected error occurred") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ChatsScreenUiState.Loading
        )

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText


    init {
        mSocket.on("receiveMessage") { messagePayload ->
            viewModelScope.launch {
                if (uiState.value is ChatsScreenUiState.Success){
                    val message = json.decodeFromString<Message>(messagePayload[0].toString())
                    if ((message.senderId == senderId && message.receiverId == receiverId) ||
                        (message.senderId == receiverId && message.receiverId == senderId)
                    ) {
                        offlineFirstMessageRepository.insertMessage(message = message)

                    }
                }
            }

        }
    }

    fun onChangeMessageText(text: String) {
        _messageText.value = text
    }

    fun sendMessage() {
        viewModelScope.launch {
            val message = Message(
                message = _messageText.value,
                isRead = false,
                senderId = senderId,
                receiverId = receiverId ,
                sentAt = "",
                messageId = UUID.randomUUID().toString()
            )
            _messageText.value = ""

            mSocket.emit("sendMessage", Gson().toJson(message))

        }

    }
}