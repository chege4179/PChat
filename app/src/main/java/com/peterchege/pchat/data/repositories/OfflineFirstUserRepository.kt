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
package com.peterchege.pchat.data.repositories

import com.peterchege.pchat.data.api.PChatApi
import com.peterchege.pchat.data.api.requests.AddUser
import com.peterchege.pchat.data.api.responses.AddUserResponse
import com.peterchege.pchat.data.api.responses.GetUserByIdResponse
import com.peterchege.pchat.data.api.responses.SearchUserResponse
import com.peterchege.pchat.data.room.database.PChatDatabase
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.domain.repository.UserRepository
import javax.inject.Inject

class OfflineFirstUserRepository @Inject constructor(
    private val api: PChatApi,
    private val db:PChatDatabase
): UserRepository{
    override suspend fun addUser(addUser: AddUser): AddUserResponse {
        return api.addUser(addUser = addUser)
    }
    override suspend fun searchUser(query:String): SearchUserResponse {
        return api.searchUser(query = query)
    }
    override suspend fun getUserById(id:String): GetUserByIdResponse {
        val localChat = db.chatDao.getChatById(receiverGoogleId = id)
        return if (localChat == null){
            api.getUserById(id = id)
        }else{
            GetUserByIdResponse(
                msg = "User fetched successfully",
                success = true,
                user = User(
                    displayName = localChat.name,
                    email = localChat.email,
                    imageUrl = localChat.imageUrl,
                    userId = localChat.googleId
                )

            )
        }

    }
}