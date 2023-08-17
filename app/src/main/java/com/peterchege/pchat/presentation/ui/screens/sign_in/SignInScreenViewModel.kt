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
package com.peterchege.pchat.presentation.ui.screens.sign_in

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.peterchege.pchat.core.api.NetworkResult
import com.peterchege.pchat.core.api.requests.AddUser
import com.peterchege.pchat.core.datastore.repository.DefaultAuthDataProvider
import com.peterchege.pchat.core.datastore.repository.DefaultFCMProvider
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val offlineFirstChatRepository: OfflineFirstChatRepository,
    private val defaultFCMProvider: DefaultFCMProvider,
    private val authRepository: AuthRepository

    ) : ViewModel() {
    private var _user = mutableStateOf<User?>(null)
    var user: State<User?> = _user

    private var _text = mutableStateOf("")
    var text: State<String> = _text

    private var _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onChangeUser(user: User) {
        addUserToDatabase(user = user)
    }


    private fun addUserToDatabase(user: User) {
        viewModelScope.launch {
            val token = FirebaseMessaging.getInstance().token.await()
            val addUser = AddUser(
                displayName = user.displayName ?: "",
                email = user.email ?: "",
                imageUrl = user.imageUrl,
                googleId = user.userId,
                deviceToken = token
            )
            val response = authRepository.addUser(addUser = addUser)
            when(response){
                is NetworkResult.Success -> {
                    if (response.data.user != null) {
                        defaultFCMProvider.setFCMToken(response.data.deviceId)
                        authRepository.setAuthUser(user = response.data.user)
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Login Successful"))
                        _eventFlow.emit(UiEvent.Navigate(Screens.DASHBOARD_SCREEN))
                    }
                }
                is NetworkResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "Please check your internet connection"))
                }
                is NetworkResult.Exception -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An unexpected error occurred"))
                }
            }
        }
    }
}