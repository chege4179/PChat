package com.peterchege.pchat.domain.repository

import com.peterchege.pchat.data.api.responses.GetChatsResponse
import com.peterchege.pchat.data.api.responses.GetMessagesResponse

interface ChatRepository {


    suspend fun getChats(email:String): GetChatsResponse

    suspend fun getChatMessages(senderEmail:String,receiverEmail:String): GetMessagesResponse
}