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
package com.peterchege.pchat.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity
import com.peterchege.pchat.domain.models.NetworkUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface ChatDao {

    @Query("SELECT * FROM chats")
    suspend fun getLocalChats(): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats:List<ChatEntity> )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM chats WHERE userId = :userId")
    suspend fun getChatById(userId: String): ChatEntity?


    fun getMessagesBetweenTwoUsers(
        senderId: String,
        receiverId: String
    ): Flow<List<MessageEntity>> {
        val messages1 =
            getMessagesFromSenderAndReceiver(senderId = senderId, receiverId = receiverId)
        val messages2 =
            getMessagesFromSenderAndReceiver(senderId = receiverId, receiverId = senderId)
        return messages2.combine(messages1) { i, s ->
            return@combine i + s
        }
    }

    @Query("SELECT * FROM messages WHERE senderId =:senderId AND receiverId =:receiverId")
    fun getMessagesFromSenderAndReceiver(
        senderId: String,
        receiverId: String
    ): Flow<List<MessageEntity>>


    @Query("DELETE FROM chats")
    suspend fun clearChats()

    @Query("DELETE FROM messages")
    suspend fun clearMessages()


}