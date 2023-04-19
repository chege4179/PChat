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
package com.peterchege.pchat.data

import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.core.api.responses.GetMessagesResponse
import com.peterchege.pchat.core.datastore.repository.UserInfoRepository
import com.peterchege.pchat.core.di.DefaultDispatcher
import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.local.LocalUserDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteUserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class OfflineFirstChatRepository @Inject constructor(
    private val localChatsDataSource: LocalChatsDataSource,
    private val remoteChatsDataSource: RemoteChatsDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val remoteUserDataSource: RemoteUserDataSource,

    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ChatRepository {


    override fun getChats():Flow<List<ChatEntity>> = flow {
        val userInfo = localUserDataSource.getAuthUser()
        val localChats = localChatsDataSource.getChats()
        emit(localChats)
        if (localChats.isEmpty()) {
            try {
                val remoteUserInfo = remoteUserDataSource.getUserById(id = userInfo.firstOrNull()?.userId ?:"")
                insertChats(chats = remoteUserInfo.chats)
                val newlocalChats = localChatsDataSource.getChats()
                emit(newlocalChats)

            } catch (e: HttpException) {
                emit(emptyList())
            }
        } else {
            val remoteUserInfo = remoteUserDataSource.getUserById(id = userInfo.firstOrNull()?.userId ?:"")
            localChatsDataSource.clearChats()
            insertChats(chats = remoteUserInfo.chats)
            val refreshedLocalChats = localChatsDataSource.getChats()
            emit(refreshedLocalChats)
        }
    }


    override suspend fun insertChats(chats: List<NetworkUser>) = withContext(ioDispatcher) {
        return@withContext localChatsDataSource.insertChats(chats = chats)

    }

    override suspend fun insertMessages(messages: List<Message>) = withContext(ioDispatcher) {
        return@withContext localChatsDataSource.insertMessages(messages = messages)
    }

    override suspend fun getAllMessagesAcrossAllChats() = withContext(ioDispatcher){
        val authUser = localUserDataSource.getAuthUser()
        authUser.collectLatest {
            val info = remoteUserDataSource.getUserById(id = it?.userId ?:"")
            val messages = info.user.receivedMessages + info.user.sentMessages
            insertMessages(messages = messages)
        }
    }

    override fun getLastMessage(receiverId: String): Flow<MessageEntity?> {
        return localChatsDataSource.getLastMessage(receiverId = receiverId)
    }

    override fun getChatMessagesBetween2Users(
        senderId: String,
        receiverId: String
    ): Flow<List<MessageEntity>> {
        return localChatsDataSource.getSingleChatMessages(senderId = senderId, receiverId = receiverId)

    }

    override suspend fun clearChats() = withContext(ioDispatcher) {
        localChatsDataSource.clearChats()

    }
}