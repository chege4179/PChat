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
package com.peterchege.pchat.presentation.ui.screens.dashboard.chat.all_chats_screen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.pchat.domain.models.ChatItem
import com.peterchege.pchat.data.repositories.OfflineFirstChatRepository
import com.peterchege.pchat.domain.mappers.asExternalModel
import com.peterchege.pchat.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class AllChatsScreenViewModel @Inject constructor(
    private val offlineFirstChatRepository: OfflineFirstChatRepository,
    private val sharedPreferences: SharedPreferences,

    ) : ViewModel() {
    val email = sharedPreferences.getString(Constants.USER_EMAIL, null)

    private val _isFound = mutableStateOf(true)
    val isFound: State<Boolean> = _isFound

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

    private val _chats = mutableStateOf<List<ChatItem>>(emptyList())
    val chats: State<List<ChatItem>> = _chats

    init {
        getChats()
    }

    private fun getChats() {
        viewModelScope.launch {
            try {
                val response = offlineFirstChatRepository.getChats(email = email!!)
                response.chats.map { chatItem ->
                    offlineFirstChatRepository.insertChatToDB(chatItem)
                }
                val localChats = offlineFirstChatRepository.getLocalChats().map {
                    val messages = offlineFirstChatRepository.getSingleChatMessages(
                        sender = email!!,
                        receiver = it.email
                    ).map {
                        it.asExternalModel()
                    }
                    it.asExternalModel(messages = messages)
                }
                _isLoading.value = false
                _isError.value = false
                _chats.value = localChats

            } catch (e: HttpException) {
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage ?: "An unexpected error occurred"
                Log.e("http error", e.localizedMessage ?: "a http error occurred")

            } catch (e: IOException) {
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = e.localizedMessage ?: "An unexpected error occurred"
                Log.e("io error", e.localizedMessage ?: "a http error occurred")
            }
        }
    }


}