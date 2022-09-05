package com.peterchege.pchat.api.requests

data class AddUser(
    val displayName:String,
    val email:String,
    val imageUrl:String,
    val userId:String,
)
