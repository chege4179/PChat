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
package com.peterchege.pchat.data.local

import com.google.firebase.auth.FirebaseAuth
import com.peterchege.pchat.core.datastore.repository.UserInfoRepository
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.local.LocalUserDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalUsersDataSourceImpl @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val db:PChatDatabase,
    private val auth:FirebaseAuth,
):LocalUserDataSource {
    override fun getAuthUser(): Flow<NetworkUser?> {
        return userInfoRepository.getLoggedInUser()
    }

    override suspend fun signOutUser() {
        auth.signOut()
        return userInfoRepository.unsetLoggedInUser()
    }

    override suspend fun setAuthUser(user: NetworkUser) {
        return userInfoRepository.setLoggedInUser(user = user)
    }

    override suspend fun getChatUserById(userId:String):NetworkUser? {
        return db.chatDao.getChatUserById(userId = userId)?.toExternalModel()
    }
}