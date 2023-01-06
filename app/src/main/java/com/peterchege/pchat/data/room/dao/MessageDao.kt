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