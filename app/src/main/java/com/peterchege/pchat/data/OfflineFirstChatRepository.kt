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
import com.peterchege.pchat.core.api.responses.SearchUserResponse
import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.core.util.DataResult
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstChatRepository @Inject constructor(
    private val remoteChatsDataSource: RemoteChatsDataSource,
    private val localChatsDataSource: LocalChatsDataSource,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
): ChatRepository{

    override fun getAllChats(): Flow<List<NetworkUser>> {
        return localChatsDataSource.getLocalChats().map { it.map { it.toExternalModel() } }
    }


    override suspend fun searchUser(query: String): NetworkResult<SearchUserResponse> {
        return remoteChatsDataSource.searchUser(query = query)
    }

    override suspend fun getUserById(id: String): DataResult<NetworkUser>{
        val user = localChatsDataSource.getChatUserById(userId = id).first()
        if (user== null){
            val remoteUserResponse = remoteChatsDataSource.getUserById(id = id)
            when(remoteUserResponse){
                is NetworkResult.Error -> {
                    return DataResult.Error(message = "Error occurred")
                }
                is NetworkResult.Exception -> {
                    return DataResult.Error(message = "Exception occurred")
                }
                is NetworkResult.Success -> {
                    if (remoteUserResponse.data.user != null){
                        return DataResult.Success(data = remoteUserResponse.data.user)
                    }else{
                        return DataResult.Error(message = "User not found")
                    }
                }
            }
        }else{
           return DataResult.Success(data = user.toExternalModel())
        }
    }

    override suspend fun clearChats() {
        localChatsDataSource.clearChat()
    }


}