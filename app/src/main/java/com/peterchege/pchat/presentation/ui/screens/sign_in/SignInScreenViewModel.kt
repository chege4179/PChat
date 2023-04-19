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
import com.peterchege.pchat.core.api.requests.AddUser
import com.peterchege.pchat.data.OfflineFirstUserRepository
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class SignInScreenViewModel @Inject constructor(

    private val offlineFirstUserRepository: OfflineFirstUserRepository,

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
            try {
                val addUser = AddUser(
                    displayName = user.displayName ?: "",
                    email = user.email ?: "",
                    imageUrl = user.imageUrl,
                    userId = user.userId
                )
                val response = offlineFirstUserRepository.addUser(addUser = addUser)
                if (response.user != null) {
                    offlineFirstUserRepository.setAuthUser(user = response.user)
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "Login Successful"))
                    _eventFlow.emit(UiEvent.Navigate(Screens.DASHBOARD_SCREEN))
                }

            } catch (e: HttpException) {
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Please check your internet connection"))

            } catch (e: IOException) {
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "An unexpected error occurred"))

            }
        }


    }


}