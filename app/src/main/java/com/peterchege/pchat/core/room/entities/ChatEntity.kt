package com.peterchege.pchat.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val userId:String,
    val googleId:String,
    val fullName:String,
    val email:String,
    val imageUrl:String,

)