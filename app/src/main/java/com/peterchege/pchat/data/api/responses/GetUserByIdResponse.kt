package com.peterchege.pchat.data.api.responses

import com.peterchege.pchat.domain.models.User

data class GetUserByIdResponse (
    val msg:String,
    val success:Boolean,
    val user: User?
        )