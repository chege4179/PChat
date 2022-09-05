package com.peterchege.pchat.repositories

import com.peterchege.pchat.api.PChatApi
import com.peterchege.pchat.api.responses.GetChatsResponse
import com.peterchege.pchat.api.responses.GetMessagesResponse
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api:PChatApi
){
    suspend fun getChats(email:String):GetChatsResponse{
        return api.getChats(email = email)

    }
    suspend fun getChatMessages(senderEmail:String,receiverEmail:String):GetMessagesResponse{
        return api.getChatMessages(senderEmail = senderEmail, receiverEmail = receiverEmail)

    }
}