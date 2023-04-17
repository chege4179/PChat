package com.peterchege.pchat.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val messageId:String,
    val message:String,
    val receiverId:String,
    val senderId:String,
    val sentAt:String,
    val isRead:Boolean
)