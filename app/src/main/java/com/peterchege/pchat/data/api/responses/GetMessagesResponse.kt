package com.peterchege.pchat.data.api.responses

import com.peterchege.pchat.domain.models.Message

data class GetMessagesResponse (
    val msg:String,
    val success:Boolean,
    val messages:List<Message>,

    )