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
package com.peterchege.pchat.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.core.api.requests.AddUser
import com.peterchege.pchat.core.api.responses.AddUserResponse
import com.peterchege.pchat.core.api.responses.GetUserByIdResponse
import com.peterchege.pchat.core.api.responses.SearchUserResponse
import com.peterchege.pchat.core.datastore.repository.UserInfoRepository
import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstUserRepository @Inject constructor(
    private val api: PChatApi,
    private val db: PChatDatabase,
    private val auth:FirebaseAuth,
    private val userInfoRepository: UserInfoRepository,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
): UserRepository{

    override suspend fun addUser(addUser: AddUser): AddUserResponse = withContext(ioDispatcher) {
        return@withContext api.addUser(addUser = addUser)
    }
    override suspend fun searchUser(query:String): SearchUserResponse = withContext(ioDispatcher) {
        return@withContext api.searchUser(query = query)
    }
    override suspend fun getUserById(id:String): GetUserByIdResponse = withContext(ioDispatcher) {
        return@withContext api.getUserById(id = id)
    }

    override fun getAuthUser(): Flow<NetworkUser?> {
        return userInfoRepository.getLoggedInUser()
    }
    override suspend fun setAuthUser(user:NetworkUser){
        return userInfoRepository.setLoggedInUser(user =  user)
    }

    override fun signOutUser(){
        auth.signOut()
    }
}