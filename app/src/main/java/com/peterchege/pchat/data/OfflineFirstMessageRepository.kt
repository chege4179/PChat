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

import com.peterchege.pchat.core.di.DefaultDispatcher
import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.MessageRepository
import com.peterchege.pchat.domain.repository.local.LocalMessagesDataSource
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteMessagesDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstMessageRepository @Inject constructor(
    private val localMessagesDataSource: LocalMessagesDataSource,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
) : MessageRepository {
    override suspend fun insertMessages(messages: List<Message>) {
        localMessagesDataSource.insertMessages(messages = messages.map { it.toEntity() })
    }

    override suspend fun insertMessage(message: Message) {
        localMessagesDataSource.insertMessage(message.toEntity())
    }

    override fun getAllMessagesAcrossAllChats(): Flow<List<Message>> {
        return localMessagesDataSource.getAllMessages()
            .map { messageEntities -> messageEntities.map { it.toExternalModel() } }
    }

    override fun getLastMessage(receiverId: String): Flow<MessageEntity?> {
        return localMessagesDataSource.getLastMessage(receiverId = receiverId)
    }

    override fun getChatMessagesBetween2Users(
        senderId: String,
        receiverId: String
    ): Flow<List<MessageEntity>> {
        return localMessagesDataSource.getMessagesBetweenTwoUsers(
            senderId = senderId,
            receiverId = receiverId
        )
    }

    override suspend fun clearMessages() {
        localMessagesDataSource.clearMessages()
    }


}