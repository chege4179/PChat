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
import com.peterchege.pchat.core.api.NetworkResult
import com.peterchege.pchat.core.util.DataResult
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

sealed interface AddChatUiState {
    object Idle :AddChatUiState

    object Loading:AddChatUiState

    data class ResultsFound(val users:List<NetworkUser>):AddChatUiState

    data class Error(val message:String) :AddChatUiState
}
@HiltViewModel
class AddChatScreenViewModel @Inject constructor(
    private val offlineFirstChatRepository: OfflineFirstChatRepository,
    private val authRepository: AuthRepository
    ):ViewModel() {
    val authUser = authRepository.getAuthUser()
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5000L)
        )

    val _uiState = MutableStateFlow<AddChatUiState>(AddChatUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var _searchTerm = mutableStateOf("")
    var searchTerm: State<String> = _searchTerm

    private var searchJob : Job? = null

    fun onChangeSearchTerm(searchTerm: String){
        _uiState.value = AddChatUiState.Loading

        _searchTerm.value = searchTerm
        if (searchTerm.length > 3){
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                val response = offlineFirstChatRepository.searchUser(query = searchTerm)
                when(response){
                    is NetworkResult.Success -> {
                        _uiState.value = AddChatUiState.ResultsFound(users = response.data.users)
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = AddChatUiState.Error(message = "An error occurred")
                    }
                    is NetworkResult.Exception -> {
                        _uiState.value = AddChatUiState.Error(message = "An exception occurred")
                    }
                }

            }
        }else if (searchTerm.length < 2){
            viewModelScope.launch {
                delay(500L)
                _uiState.value = AddChatUiState.Error(message = "Please input more than 2 characters")
            }
        }
    }
}