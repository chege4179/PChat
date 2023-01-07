/*
 * Copyright 2023 PChat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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