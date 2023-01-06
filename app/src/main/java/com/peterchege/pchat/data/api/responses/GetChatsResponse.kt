package com.peterchege.pchat.data.api.responses

import com.peterchege.pchat.domain.models.ChatItem

data class GetChatsResponse (
    val msg:String,
    val success:String,
    val chats:List<ChatItem>,
    )