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
package com.peterchege.pchat.presentation.ui.screens.dashboard

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.pchat.data.repositories.OfflineFirstChatRepository
import com.peterchege.pchat.data.repositories.OfflineFirstUserRepository
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.SocketHandler.mSocket
import com.peterchege.pchat.util.getGoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val offlineFirstChatRepository: OfflineFirstChatRepository,
):ViewModel() {
    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> =_isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg: State<String> =_errorMsg

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> =_isLoading

    private val _msg = mutableStateOf("")
    val msg: State<String> = _msg

    val displayName = sharedPreferences.getString(Constants.USER_DISPLAY_NAME,null)
    val imageUrl = sharedPreferences.getString(Constants.USER_IMAGE_URL,null)
    val email = sharedPreferences.getString(Constants.USER_EMAIL,null)

    init{
        mSocket.emit("connected","$email has connected")

    }



    fun logoutUser(navController:NavController,context: Context){
        val signInClient = getGoogleSignInClient(context = context)

        sharedPreferences.edit().remove(Constants.USER_ID).commit()
        sharedPreferences.edit().remove(Constants.USER_DISPLAY_NAME).commit()
        sharedPreferences.edit().remove(Constants.USER_EMAIL).commit()
        sharedPreferences.edit().remove(Constants.USER_IMAGE_URL).commit()
        signInClient.signOut()
        viewModelScope.launch {
            offlineFirstChatRepository.clearChats()
            offlineFirstChatRepository.clearMessages()
        }
        navController.navigate(Screens.SIGN_IN_SCREEN)


    }

}