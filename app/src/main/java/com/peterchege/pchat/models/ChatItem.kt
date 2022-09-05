package com.peterchege.pchat.models

data class ChatItem(
    val id:String,
    val name:String,
    val email:String,
    val imageUrl:String,
    val userId:String,
    val messages:List<Message>,

)