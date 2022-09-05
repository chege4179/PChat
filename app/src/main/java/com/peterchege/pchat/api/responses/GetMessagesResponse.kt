package com.peterchege.pchat.api.responses

import com.peterchege.pchat.models.Message

data class GetMessagesResponse (
    val msg:String,
    val success:Boolean,
    val messages:List<Message>,

        )