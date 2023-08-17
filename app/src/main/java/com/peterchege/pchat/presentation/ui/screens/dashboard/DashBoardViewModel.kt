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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.pchat.core.work.sync_chats.SyncChatsWorkManager
import com.peterchege.pchat.core.work.sync_messages.SyncMessagesWorkManager
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.data.OfflineFirstMessageRepository
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.domain.repository.MessageRepository
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.util.SocketHandler.mSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val offlineFirstChatRepository: OfflineFirstChatRepository,
    private val offlineFirstMessageRepository: OfflineFirstMessageRepository,
    private val authRepository: AuthRepository,
    private val syncChatsWorkManager: SyncChatsWorkManager,
    private val syncMessagesWorkManager: SyncMessagesWorkManager
) : ViewModel() {
    val authUser = authRepository.getAuthUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val isMessagesSyncing = syncMessagesWorkManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )
    val isChatsSyncing = syncChatsWorkManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    init {
        startRefreshingChats()
        mSocket.emit("connected", " has connected")

    }

    fun startRefreshingChats(){
        viewModelScope.launch {
            val syncChatsResults = async { syncChatsWorkManager.startSyncChats() }
            val syncMessagesWorker = async { syncMessagesWorkManager.startSyncMessages() }
            syncChatsResults.await()
            syncMessagesWorker.await()

        }
    }


}