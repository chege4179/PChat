package com.peterchege.pchat.ui.screens.dashboard.chat.chat_screen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.peterchege.pchat.api.requests.AddUser
import com.peterchege.pchat.models.Message
import com.peterchege.pchat.models.Room
import com.peterchege.pchat.models.User
import com.peterchege.pchat.repositories.UserRepository
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.SocketHandler.mSocket
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionStateChange
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
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,

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

    init {
        savedStateHandle.get<String>("id")?.let {
            getUserById(id = it)

        }

        mSocket.on("roomJoined") { room ->
            Log.e("Room Joined", "RoomJoined")
            if (room[0] != null) {
                val roomId = room[0] as String
                Log.e("rooom Id", roomId)
                _roomId.value = roomId

            }
        }
        mSocket.on("receiveMessage") { messagePayload ->
            val message = Json.decodeFromString<Message>(messagePayload[0].toString())
            Log.e("message received",message.message)
        }
    }




    fun onChangeMessageText(text:String){
        _messageText.value = text
    }

    private fun getUserById(id:String){
        viewModelScope.launch {
            try{
                val response = userRepository.getUserById(id = id)
                if (response.success){
                    _activeChatUser.value = response.user
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
            id=null,
            isRead = true
        )
        mSocket.emit("sendMessage", Gson().toJson(message))


    }
}