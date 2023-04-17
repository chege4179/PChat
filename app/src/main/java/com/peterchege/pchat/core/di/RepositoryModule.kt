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
package com.peterchege.pchat.core.di

import com.google.firebase.auth.FirebaseAuth
import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.core.datastore.repository.UserInfoRepository
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.data.OfflineFirstUserRepository
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        api: PChatApi,
        db: PChatDatabase,
        userRepository: UserInfoRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ChatRepository {
        return OfflineFirstChatRepository(
            api = api,
            db = db,
            ioDispatcher = ioDispatcher,
            userInfoRepository = userRepository
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: PChatApi,
        db: PChatDatabase,
        auth:FirebaseAuth,
        userRepository: UserInfoRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): UserRepository {
        return OfflineFirstUserRepository(
            api = api,
            db = db,
            ioDispatcher = ioDispatcher,
            auth = auth,
            userInfoRepository = userRepository
        )
    }
}