package com.peterchege.pchat.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peterchege.pchat.room.dao.UserDao
import com.peterchege.pchat.room.entities.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class PChatDatabase: RoomDatabase() {

    abstract val userDao:UserDao

}