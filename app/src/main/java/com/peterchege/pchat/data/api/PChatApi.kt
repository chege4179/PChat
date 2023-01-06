package com.peterchege.pchat.data.api

import com.peterchege.pchat.data.api.requests.AddUser
import com.peterchege.pchat.data.api.responses.*
import retrofit2.http.*

interface PChatApi {
    @POST("/user/add")
    suspend fun addUser(@Body addUser: AddUser): AddUserResponse

    @GET("/user/search")
    suspend fun searchUser(@Query("query") query:String): SearchUserResponse

    @GET("/user/single/{id}")
    suspend fun getUserById(@Path("id") id:String): GetUserByIdResponse

    @GET("/chat/mychats/{email}")
    suspend fun getChats(@Path("email") email:String): GetChatsResponse

    @GET("/chat/message/{senderEmail}/{receiverEmail}")
    suspend fun getChatMessages(
        @Path("senderEmail") senderEmail:String,
        @Path("receiverEmail")receiverEmail:String
    ): GetMessagesResponse



}