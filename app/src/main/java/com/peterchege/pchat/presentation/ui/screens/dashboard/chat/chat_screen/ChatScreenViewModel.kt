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

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.data.OfflineFirstUserRepository
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.util.SocketHandler.mSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.IOException
import retrofit2.HttpException
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val offlineFirstUserRepository: OfflineFirstUserRepository,

    private val offlineFirstChatRepository: OfflineFirstChatRepository,

    ) : ViewModel() {
    val authUser = offlineFirstUserRepository.getAuthUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )


    private val _activeChatUserId = mutableStateOf("")
    val activeChatUserId: State<String> = _activeChatUserId

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _roomId = mutableStateOf<String?>(null)
    val roomId: State<String?> = _roomId

    private val _activeChatUser = mutableStateOf<NetworkUser?>(null)
    val activeChatUser: State<NetworkUser?> = _activeChatUser

    private val _messages = mutableStateOf<Flow<List<MessageEntity>>>(flow { emptyList<Message>() })
    val messages: State<Flow<List<MessageEntity>>> = _messages

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

    init {
        savedStateHandle.get<String>("id")?.let {
            getUserById(id = it)

        }

        mSocket.on("roomJoined") { room ->
            if (room[0] != null) {
                val roomId = room[0] as String
                Log.e("rooom Id", roomId)
                _roomId.value = roomId

            }
        }
        mSocket.on("receiveMessage") { messagePayload ->
            viewModelScope.launch {
                authUser.collectLatest {
                    if (it == null) return@collectLatest
                    val message = Json.decodeFromString<Message>(messagePayload[0].toString())
                    if ((message.senderId == it.userId && message.receiverId == activeChatUser.value!!.userId) ||
                        (message.senderId == activeChatUser.value!!.userId && message.receiverId == it.userId)
                    ) {
                        addMessage(message = message)
                    }

                    Log.e("message received", message.message)
                }

            }

        }
    }

    private fun addMessage(message: Message) {
        viewModelScope.launch {
            offlineFirstChatRepository.insertMessages(messages = listOf<Message>(message))


            lateinit var initialMessages: List<Message>;

            _messages.value.collectLatest {
                initialMessages = it.map { it.toExternalModel() }
            }

            val newList = ArrayList(initialMessages)
            newList.add(message)
            _messages.value = flow { newList.toList() }
        }

    }

    private fun getMessages() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                authUser.collectLatest {
                    val localMessages = offlineFirstChatRepository.getSingleChatMessages(
                        senderId = it!!.userId,
                        receiverId = activeChatUser.value!!.userId
                    )
                    _isLoading.value = false
                    _isError.value = false
                    _messages.value = localMessages
                }


            } catch (e: HttpException) {
                Log.e("HTTP ERROR", e.localizedMessage ?: "Http error")
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage ?: "An unexpected error occurred"

            } catch (e: IOException) {
                Log.e("IO ERROR", e.localizedMessage ?: "IO error")
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage ?: "An unexpected error occurred"
            }
        }
    }

    fun onChangeMessageText(text: String) {
        _messageText.value = text
    }

    private fun getUserById(id: String) {
        viewModelScope.launch {
            try {
                val response = offlineFirstUserRepository.getUserById(id = id)
                if (response.success) {
                    _activeChatUser.value = NetworkUser(
                        userId = response.user.userId,
                        googleId = response.user.googleId,
                        fullName = response.user.fullName,
                        email = response.user.email,
                        imageUrl = response.user.imageUrl
                    )
                    Log.e("User", response.user.toString())
                    getMessages()

                }

            } catch (e: HttpException) {
                Log.e("HTTP ERROR", e.localizedMessage ?: "Http error")

            } catch (e: IOException) {
                Log.e("IO ERROR", e.localizedMessage ?: "IO error")

            }
        }

    }

    fun sendMessage() {
        viewModelScope.launch {
            authUser.collectLatest {
                if (it == null) return@collectLatest
                val message = Message(
                    message = _messageText.value,
                    isRead = false,
                    senderId = it.userId,
                    receiverId = activeChatUser.value?.userId ?: "",
                    sentAt = "",
                    messageId = UUID.randomUUID().toString()
                )
                _messageText.value = ""
                mSocket.emit("sendMessage", Gson().toJson(message))
            }

        }

    }
}