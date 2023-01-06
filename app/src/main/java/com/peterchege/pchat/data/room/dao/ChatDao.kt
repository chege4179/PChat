package com.peterchege.pchat.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peterchege.pchat.data.room.entities.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats")
    suspend fun getLocalChats():List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chatEntity: ChatEntity)


    @Query("DELETE FROM chats")
    suspend fun clearChats()

}