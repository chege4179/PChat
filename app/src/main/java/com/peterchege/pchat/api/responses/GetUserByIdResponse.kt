package com.peterchege.pchat.api.responses

import com.peterchege.pchat.models.User

data class GetUserByIdResponse (
    val msg:String,
    val success:Boolean,
    val user:User?
        )