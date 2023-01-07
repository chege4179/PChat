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
package com.peterchege.pchat.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peterchege.pchat.data.room.entities.MessageEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages")
    suspend fun getLocalMessage(): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE receiver = :receiver AND sender = :sender ")
    suspend fun getLocalMessagesByChat(receiver: String, sender: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE receiver = :sender AND sender = :receiver")
    suspend fun getLocalMessagesByChat2(receiver: String, sender: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE receiver = :receiver AND sender = :sender ORDER BY sentAt,sentOn DESC LIMIT 1 ")
    suspend fun getLastMessageByChat(receiver: String, sender: String): MessageEntity

    @Query("DELETE FROM messages")
    suspend fun clearMessages()




}