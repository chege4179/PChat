package com.peterchege.pchat.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chats")
data class ChatEntity (
    @PrimaryKey
    val chatId:String,
    val name:String,
    val googleId:String,
    val email:String,
    val imageUrl:String,
    )