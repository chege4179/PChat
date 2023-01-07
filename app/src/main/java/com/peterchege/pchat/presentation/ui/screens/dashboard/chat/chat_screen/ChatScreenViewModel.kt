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

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.Room
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.data.repositories.OfflineFirstChatRepository
import com.peterchege.pchat.data.repositories.OfflineFirstUserRepository
import com.peterchege.pchat.domain.mappers.asExternalModel
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.SocketHandler.mSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.IOException
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val offlineFirstUserRepository: OfflineFirstUserRepository,
    private val sharedPreferences: SharedPreferences,
    private val offlineFirstChatRepository: OfflineFirstChatRepository,

    ) :ViewModel(){
    val displayName = sharedPreferences.getString(Constants.USER_DISPLAY_NAME,null)
    val imageUrl = sharedPreferences.getString(Constants.USER_IMAGE_URL,null)
    val email = sharedPreferences.getString(Constants.USER_EMAIL,null)

    private val _activeChatUserId = mutableStateOf("")
    val activeChatUserId: State<String> =_activeChatUserId

    private val _messageText = mutableStateOf("")
    val messageText: State<String> =_messageText

    private val _roomId = mutableStateOf<String?>(null)
    val roomId: State<String?> =_roomId

    private val _activeChatUser = mutableStateOf<User?>(null)
    val activeChatUser: State<User?> =_activeChatUser

    private val _messages = mutableStateOf<List<Message>>(emptyList())
    val messages :State<List<Message>> = _messages

    private val _isLoading = mutableStateOf(false)
    val isLoading : State<Boolean> = _isLoading

    private val _isError = mutableStateOf(false)
    val isError : State<Boolean> = _isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg: State<String> =_errorMsg

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
            val message = Json.decodeFromString<Message>(messagePayload[0].toString())
            if( (message.sender ==email && message.receiver == activeChatUser.value!!.email) ||
                (message.sender == activeChatUser.value!!.email && message.receiver == email)){
                addMessage(message = message)
            }

            Log.e("message received",message.message)
        }
    }
    private fun addMessage(message: Message) {
        viewModelScope.launch {
            offlineFirstChatRepository.insertMessage(message = message)
        }


        val newList = ArrayList(_messages.value)
        newList.add(message)
        _messages.value = newList
    }
    private fun getMessages(){
        _isLoading.value = true
        viewModelScope.launch {
            try{
//                val response = offlineFirstChatRepository.getChatMessages(
//                    senderEmail = email!!,
//                    receiverEmail = activeChatUser.value!!.email
//                )
                val localMessages = offlineFirstChatRepository.getSingleChatMessages(
                    sender = email!!,
                    receiver = activeChatUser.value!!.email
                )
                _isLoading.value = false
                _isError.value = false
                _messages.value = localMessages.map { it.asExternalModel() }
//                if (response.success){
//                    _isLoading.value = false
//                    _isError.value = false
//                    _messages.value = response.messages
//                }
            }catch (e: HttpException){
                Log.e("HTTP ERROR",e.localizedMessage ?: "Http error")
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage ?: "An unexpected error occurred"

            }catch (e:IOException){
                Log.e("IO ERROR",e.localizedMessage ?: "IO error")
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage ?: "An unexpected error occurred"
            }
        }
    }
    fun onChangeMessageText(text:String){
        _messageText.value = text
    }

    private fun getUserById(id:String){
        viewModelScope.launch {
            try{
                val response = offlineFirstUserRepository.getUserById(id = id)
                if (response.success){
                    _activeChatUser.value = response.user
                    Log.e("User",response.user.toString())
                    getMessages()
                    val room = Room(
                        user1 = email!!,
                        user2 = response.user!!.email
                    )
                    mSocket.emit("joinRoom",Gson().toJson(room))
                }

            }catch (e: HttpException){
                Log.e("HTTP ERROR",e.localizedMessage ?: "Http error")

            }catch (e:IOException){
                Log.e("IO ERROR",e.localizedMessage ?: "IO error")

            }
        }

    }

    fun sendMessage(){
        val message = Message(
            message = _messageText.value,
            sender =  email!!,
            sentOn = SimpleDateFormat("dd/MM/yyyy").format(Date()),
            sentAt = SimpleDateFormat("hh:mm:ss").format(Date()),
            receiver = _activeChatUser.value!!.email,
            isMine = true,
            id= UUID.randomUUID().toString(),
            isRead = true
        )
        _messageText.value = ""
        mSocket.emit("sendMessage", Gson().toJson(message))
    }
}