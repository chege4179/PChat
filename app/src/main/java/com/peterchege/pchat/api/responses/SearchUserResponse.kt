package com.peterchege.pchat.api.responses

import com.peterchege.pchat.models.User

data class SearchUserResponse(
    val msg:String,
    val success:String,
    val users:List<User>
)