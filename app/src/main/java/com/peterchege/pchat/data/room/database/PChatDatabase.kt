package com.peterchege.pchat.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peterchege.pchat.data.room.dao.ChatDao
import com.peterchege.pchat.data.room.dao.MessageDao
import com.peterchege.pchat.data.room.entities.ChatEntity
import com.peterchege.pchat.data.room.entities.MessageEntity

@Database(
    entities = [ChatEntity::class, MessageEntity::class],
    version = 1
)
abstract class PChatDatabase: RoomDatabase() {

    abstract val chatDao: ChatDao
    abstract val messageDao: MessageDao

}