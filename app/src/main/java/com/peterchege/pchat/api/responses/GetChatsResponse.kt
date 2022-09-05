package com.peterchege.pchat.api.responses

import com.peterchege.pchat.models.ChatItem

data class GetChatsResponse (
    val msg:String,
    val success:String,
    val chats:List<ChatItem>,
    )