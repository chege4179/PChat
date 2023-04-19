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
package com.peterchege.pchat.presentation.ui.screens.add_chat_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.pchat.data.OfflineFirstUserRepository
import com.peterchege.pchat.domain.uiState.AddChat
import com.peterchege.pchat.domain.uiState.AddChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class AddChatScreenViewModel @Inject constructor(
    private val offlineFirstUserRepository: OfflineFirstUserRepository,

):ViewModel() {

    val _uiState = MutableStateFlow<AddChatUiState>(
        AddChatUiState.Idle(message = "Search Users you'd like to chat with"))
    val uiState = _uiState.asStateFlow()

    private var _searchTerm = mutableStateOf("")
    var searchTerm: State<String> = _searchTerm


    private var searchJob : Job? = null

    fun onChangeSearchTerm(searchTerm: String){
        _uiState.value = AddChatUiState.Loading(message = "Loading....")

        _searchTerm.value = searchTerm
        if (searchTerm.length > 3){
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                try {
                    val response = offlineFirstUserRepository.searchUser(query = searchTerm)
                    _uiState.value = AddChatUiState.Success(data = AddChat(searchUsers = response.users))

                }catch (e: HttpException){
                    val msg = e.localizedMessage?: "A server exception has occurred"
                    _uiState.value = AddChatUiState.Error(errorMessage = msg)

                }catch (e:IOException){
                    val msg = e.localizedMessage?: "An unexpected error occurred"
                    _uiState.value = AddChatUiState.Error(errorMessage = msg)
                }
            }
        }else if (searchTerm.length < 2){
            viewModelScope.launch {
                delay(500L)
                _uiState.value = AddChatUiState.Error(errorMessage = "Please input more than 2 characters")
            }
        }
    }
}