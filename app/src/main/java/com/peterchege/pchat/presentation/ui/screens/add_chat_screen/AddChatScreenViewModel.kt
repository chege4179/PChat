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

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.data.OfflineFirstUserRepository
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class AddChatScreenViewModel @Inject constructor(
    private val offlineFirstUserRepository: OfflineFirstUserRepository,


    ):ViewModel() {
    val authUser = offlineFirstUserRepository.getAuthUser()


    private var _searchTerm = mutableStateOf("")
    var searchTerm: State<String> = _searchTerm

    private val _isFound = mutableStateOf(true)
    val isFound : State<Boolean> = _isFound

    private val _isLoading = mutableStateOf(false)
    val isLoading : State<Boolean> = _isLoading

    private val _isError = mutableStateOf(false)
    val isError : State<Boolean> = _isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg : State<String> = _errorMsg

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private var searchJob : Job? = null

    private val _searchUsers = mutableStateOf<List<NetworkUser>>(emptyList())
    val searchUser :State<List<NetworkUser>> = _searchUsers


    fun onChangeSearchTerm(searchTerm: String){
        _isLoading.value = true
        _searchTerm.value = searchTerm
        if (searchTerm.length > 3){

            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                try {
                    val response = offlineFirstUserRepository.searchUser(query = searchTerm)

                    _isLoading.value = false
                    _searchUsers.value = response.users
//                        .filter { it.email != email }

                }catch (e: HttpException){
                    _isLoading.value = false
                    Log.e("http error",e.localizedMessage?: "a http error occurred")

                }catch (e:IOException){
                    _isLoading.value = false
                    Log.e("io error",e.localizedMessage?: "a http error occurred")

                }

            }
        }else if (searchTerm.length < 2){
            viewModelScope.launch {
                delay(500L)
                _isFound.value = true

            }

        }
    }


}