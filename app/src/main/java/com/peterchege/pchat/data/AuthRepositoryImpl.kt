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

import com.peterchege.pchat.core.api.NetworkResult
import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.core.api.requests.AddUser
import com.peterchege.pchat.core.api.responses.AddUserResponse
import com.peterchege.pchat.core.api.safeApiCall
import com.peterchege.pchat.core.datastore.repository.DefaultAuthDataProvider
import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val api: PChatApi,
    private val defaultAuthDataProvider: DefaultAuthDataProvider,
):AuthRepository {
    override suspend fun addUser(addUser: AddUser):NetworkResult<AddUserResponse> {
        return withContext(ioDispatcher){
            safeApiCall { api.addUser(addUser = addUser) }
        }
    }

    override fun getAuthUser(): Flow<NetworkUser?> {
        return defaultAuthDataProvider.getLoggedInUser().flowOn(ioDispatcher)
    }

    override suspend fun setAuthUser(user: NetworkUser) {
        withContext(ioDispatcher){
            defaultAuthDataProvider.setLoggedInUser(user = user)
        }
    }

    override suspend fun signOutUser() {
        withContext(ioDispatcher){
            defaultAuthDataProvider.unsetLoggedInUser()
        }
    }


}