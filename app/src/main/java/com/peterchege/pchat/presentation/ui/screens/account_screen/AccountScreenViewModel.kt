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
package com.peterchege.pchat.presentation.ui.screens.account_screen

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.domain.repository.UserRepository
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.getGoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountScreenViewModel @Inject constructor(

    private val offlineFirstUserRepository: UserRepository,
    private val offlineFirstChatRepository: ChatRepository

) :ViewModel(){
    val authUser = offlineFirstUserRepository.getAuthUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun logoutUser(navController: NavController, context: Context){
        val signInClient = getGoogleSignInClient(context = context)
        signInClient.signOut()
        offlineFirstUserRepository.signOutUser()
        viewModelScope.launch {
            offlineFirstChatRepository.clearChats()
        }
        navController.navigate(Screens.SIGN_IN_SCREEN)


    }


}