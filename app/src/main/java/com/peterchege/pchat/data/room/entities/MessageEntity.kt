package com.peterchege.pchat.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class MessageEntity (
    @PrimaryKey
    val messageId:String,
    val sender:String,
    val receiver:String,
    val sentAt:String,
    val sentOn:String,
    val isMine:Boolean,
    val isRead:Boolean,
    val message:String,
    )