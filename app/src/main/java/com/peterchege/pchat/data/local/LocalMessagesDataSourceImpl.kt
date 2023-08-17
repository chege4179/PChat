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
package com.peterchege.pchat.data.local

import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.local.LocalMessagesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalMessagesDataSourceImpl @Inject constructor(
    private val db:PChatDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
):LocalMessagesDataSource{
    override suspend fun insertMessages(messages: List<MessageEntity>) {
        withContext(ioDispatcher){
            db.messageDao.insertMessages(messages = messages)
        }
    }

    override suspend fun insertMessage(message: MessageEntity) {
        withContext(ioDispatcher){
            db.messageDao.insertMessage(message = message)
        }
    }

    override fun getAllMessages(): Flow<List<MessageEntity>> {
        return db.messageDao.getAllMessages().flowOn(ioDispatcher)
    }

    override fun getMessagesBetweenTwoUsers(senderId: String, receiverId: String):Flow<List<MessageEntity>> {
        return db.messageDao.getMessagesBetweenTwoUsers(senderId = senderId,receiverId = receiverId)
            .flowOn(ioDispatcher)
    }

    override fun getLastMessage(receiverId: String): Flow<MessageEntity?> {
        return db.messageDao.getLastMessage(receiverId = receiverId).flowOn(ioDispatcher)
    }

    override suspend fun clearMessages() {
        withContext(ioDispatcher){
            db.messageDao.clearMessages()
        }
    }

}