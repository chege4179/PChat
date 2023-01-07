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
package com.peterchege.pchat.data.repositories

import com.peterchege.pchat.data.api.PChatApi
import com.peterchege.pchat.data.api.responses.GetChatsResponse
import com.peterchege.pchat.data.api.responses.GetMessagesResponse
import com.peterchege.pchat.data.room.database.PChatDatabase
import com.peterchege.pchat.data.room.entities.ChatEntity
import com.peterchege.pchat.data.room.entities.MessageEntity
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.models.ChatItem
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.util.generateTimestamp
import javax.inject.Inject

class OfflineFirstChatRepository @Inject constructor(
    private val api: PChatApi,
    private val db: PChatDatabase
) : ChatRepository {

    override suspend fun getChats(email: String): GetChatsResponse {
        return api.getChats(email = email)

    }

    suspend fun getLocalChats(): List<ChatEntity> {
        return db.chatDao.getLocalChats()

    }
    suspend fun insertMessage(message: Message){
        return db.messageDao.insertMessage(message = message.toEntity())
    }
    suspend fun getSingleChatMessages(sender:String,receiver:String):List<MessageEntity>{
        val localMessages1 = db.messageDao.getLocalMessagesByChat(receiver = receiver,sender = sender)
        val localMessages2  = db.messageDao.getLocalMessagesByChat2(receiver = receiver,sender = sender)
        return localMessages1.plus(localMessages2).sortedBy {
            generateTimestamp(sentAt = it.sentAt, sentOn = it.sentOn)
        }
    }
    suspend fun insertChatToDB(chatItem: ChatItem){
        db.chatDao.insertChat(chatItem.toEntity())
        chatItem.messages.map { message ->
            db.messageDao.insertMessage(message = message.toEntity())
        }
    }
    suspend fun clearChats(){
        db.chatDao.clearChats()
    }
    suspend fun clearMessages(){
        db.messageDao.clearMessages()
    }

    override suspend fun getChatMessages(
        senderEmail: String,
        receiverEmail: String
    ): GetMessagesResponse {
        return api.getChatMessages(senderEmail = senderEmail, receiverEmail = receiverEmail)

    }
}