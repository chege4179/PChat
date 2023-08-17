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
package com.peterchege.pchat.core.api


import com.peterchege.pchat.core.api.requests.AddUser
import com.peterchege.pchat.core.api.responses.*
import retrofit2.Response
import retrofit2.http.*

interface PChatApi {
    @POST("/user/login")
    suspend fun addUser(@Body addUser: AddUser):Response<AddUserResponse>

    @GET("/user/search")
    suspend fun searchUser(@Query("query") query: String): Response<SearchUserResponse>

    @GET("/user/single/{id}")
    suspend fun getUserById(@Path("id") id: String):Response<GetUserByIdResponse>

    @GET("/chat/message/{senderId}/{receiverId}")
    suspend fun getChatMessages(
        @Path("senderId") senderId: String,
        @Path("receiverId") receiverId: String
    ): Response<GetMessagesBetweenTwoUsers>

    @GET("/chat/message/all/{userId}")
    suspend fun getAllMessages(
        @Path("userId") senderId: String,
    ): Response<GetMessagesResponse>


}