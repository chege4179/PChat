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

import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalChatsDataSourceImpl @Inject constructor(
    private val db:PChatDatabase
):LocalChatsDataSource{


    override suspend fun getChats(): List<ChatEntity> {
        return db.chatDao.getLocalChats()
    }

    override fun getSingleChatMessages(
        senderId: String,
        receiverId: String
    ): Flow<List<MessageEntity>> {
        return db.chatDao.getMessagesFromSenderAndReceiver(senderId = senderId,receiverId = receiverId)
    }

    override fun getLastMessage(receiverId: String): Flow<MessageEntity?> {
        return db.chatDao.getLastMessage(receiverId = receiverId)
    }

    override suspend fun insertMessages(messages: List<Message>) {
        return db.chatDao.insertMessages(messages = messages.map { it.toEntity() })
    }

    override suspend fun clearChats() {
        return db.chatDao.clearChats()
    }

    override suspend fun insertChats(chats: List<NetworkUser>) {
        return db.chatDao.insertChats(chats = chats.map { it.toEntity() })
    }
}