package com.peterchege.pchat.ui.screens.dashboard.chat.all_chats_screen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peterchege.pchat.models.ChatItem
import com.peterchege.pchat.models.User
import com.peterchege.pchat.repositories.ChatRepository
import com.peterchege.pchat.repositories.UserRepository
import com.peterchege.pchat.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class AllChatsScreenViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val sharedPreferences: SharedPreferences,

) :ViewModel(){
    val email = sharedPreferences.getString(Constants.USER_EMAIL,null)

    private val _isFound = mutableStateOf(true)
    val isFound : State<Boolean> = _isFound

    private val _isLoading = mutableStateOf(false)
    val isLoading : State<Boolean> = _isLoading

    private val _isError = mutableStateOf(false)
    val isError : State<Boolean> = _isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg : State<String> = _errorMsg

    private val _chats = mutableStateOf<List<ChatItem>>(emptyList())
    val chats :State<List<ChatItem>> = _chats

    init {
        getChats()
    }

    private fun getChats(){
        viewModelScope.launch {
            try {
                val response = chatRepository.getChats(email = email!!)
                _isLoading.value = false
                _isError.value = false
                _chats.value = response.chats

            }catch (e: HttpException){
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage?: "An unexpected error occurred"
                Log.e("http error",e.localizedMessage?: "a http error occurred")

            }catch (e:IOException){
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage?: "An unexpected error occurred"
                Log.e("io error",e.localizedMessage?: "a http error occurred")
            }
        }
    }



}