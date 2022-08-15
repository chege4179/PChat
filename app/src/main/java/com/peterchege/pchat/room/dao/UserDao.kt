package com.peterchege.pchat.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.peterchege.pchat.room.entities.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<User>>

}