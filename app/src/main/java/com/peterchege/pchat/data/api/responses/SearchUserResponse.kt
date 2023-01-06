package com.peterchege.pchat.data.api.responses

import com.peterchege.pchat.domain.models.User

data class SearchUserResponse(
    val msg:String,
    val success:String,
    val users:List<User>
)