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
package com.peterchege.pchat.domain.repository

import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun insertMessages(messages: List<Message>)

    suspend fun insertMessage(message: Message)

    fun getAllMessagesAcrossAllChats():Flow<List<Message>>

    fun getLastMessage(receiverId: String): Flow<MessageEntity?>

    fun getChatMessagesBetween2Users(
        senderId: String,
        receiverId: String
    ): Flow<List<MessageEntity>>

    suspend fun clearMessages()






}