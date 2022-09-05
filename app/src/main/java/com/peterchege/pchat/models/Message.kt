package com.peterchege.pchat.models

import kotlinx.serialization.Serializable


@Serializable
data class Message(
    val message:String,
    val sender:String,
    val sentAt:String,
    val receiver:String,
    val id:String?,
    val isMine:Boolean,
    val sentOn:String,
    val isRead:Boolean
)