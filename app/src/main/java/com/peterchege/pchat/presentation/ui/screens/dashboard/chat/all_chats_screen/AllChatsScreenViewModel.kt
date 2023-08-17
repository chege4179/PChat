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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.data.OfflineFirstMessageRepository
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.presentation.models.ChatCardInfo
import com.peterchege.pchat.presentation.ui.screens.add_chat_screen.AddChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.zip
import javax.inject.Inject


sealed interface AllChatsScreenUiState {

    object Loading : AllChatsScreenUiState

    data class Success(val chats: List<ChatCardInfo>) : AllChatsScreenUiState

    data class Error(val message: String) : AllChatsScreenUiState
}

@HiltViewModel
class AllChatsScreenViewModel @Inject constructor(
    private val offlineFirstChatRepository: OfflineFirstChatRepository,
    private val offlineFirstMessageRepository: OfflineFirstMessageRepository,
    private val authRepository: AuthRepository,

    ) : ViewModel() {

    val uiState = combine(
        offlineFirstChatRepository.getAllChats(),
        authRepository.getAuthUser()
    ) { chats, authUser ->
        chats.map {
            val message = offlineFirstMessageRepository.getLastMessage(receiverId = it.userId)
                .map { it?.toExternalModel() }
            ChatCardInfo(authUser = authUser!!, chatUserInfo = it, lastMessage = message)
        }

    }
        .map<List<ChatCardInfo>, AllChatsScreenUiState>(AllChatsScreenUiState::Success)
        .onStart { AllChatsScreenUiState.Loading }
        .catch { AllChatsScreenUiState.Error(message = it.localizedMessage ?: "An error occurred") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AllChatsScreenUiState.Loading
        )


}
