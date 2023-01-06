package com.peterchege.pchat.data.api.requests

data class AddUser(
    val displayName:String,
    val email:String,
    val imageUrl:String,
    val userId:String,
)
