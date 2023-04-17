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
import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class OfflineFirstChatRepository @Inject constructor(
    private val api: PChatApi,
    private val db: PChatDatabase,
    private val userInfoRepository: UserInfoRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
) : ChatRepository {


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getChats(userId: String): List<ChatEntity> = withContext(ioDispatcher) {

        val userInfo = userInfoRepository.getLoggedInUser()
        val localChats = db.chatDao.getLocalChats()
        if (localChats.isEmpty()) {
            try {
                val remoteUserInfo = api.getUserById(id = userId)
                insertChats(chats = remoteUserInfo.chats)
                return@withContext db.chatDao.getLocalChats()
            } catch (e: HttpException) {
                return@withContext localChats
            }
        } else {
            val remoteUserInfo = api.getUserById(id = userId)
            db.chatDao.clearChats()
            insertChats(chats = remoteUserInfo.chats)
            return@withContext db.chatDao.getLocalChats()

        }
    }


    override suspend fun insertChats(chats: List<NetworkUser>) = withContext(ioDispatcher) {
        return@withContext db.chatDao.insertChats(chats = chats.map { it.toEntity() })

    }

    override suspend fun insertMessages(messages: List<Message>) = withContext(ioDispatcher) {
        val messageEntities = messages.map { it.toEntity() }
        return@withContext db.chatDao.insertMessages(messages = messageEntities)
    }

    override fun getSingleChatMessages(
        senderId: String,
        receiverId: String
    ): Flow<List<MessageEntity>> {
        return db.chatDao.getMessagesBetweenTwoUsers(senderId = senderId, receiverId = receiverId)
    }

    override suspend fun getChatMessages(
        senderId: String,
        receiverId: String
    ): GetMessagesResponse = withContext(ioDispatcher) {
        return@withContext api.getChatMessages(senderId = senderId, receiverId = receiverId)

    }

    override suspend fun clearChats() = withContext(ioDispatcher) {
        db.chatDao.clearMessages()
        db.chatDao.clearChats()


    }
}