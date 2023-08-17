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

import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.core.datastore.repository.DefaultAuthDataProvider
import com.peterchege.pchat.core.datastore.repository.DefaultFCMProvider
import com.peterchege.pchat.data.AuthRepositoryImpl
import com.peterchege.pchat.data.OfflineFirstMessageRepository
import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.domain.repository.MessageRepository
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.domain.repository.local.LocalMessagesDataSource
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteMessagesDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
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
    fun provideAuthRepository(
        api:PChatApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        defaultAuthDataProvider: DefaultAuthDataProvider,

    ):AuthRepository{
        return AuthRepositoryImpl(
            api = api,
            ioDispatcher = ioDispatcher,
            defaultAuthDataProvider = defaultAuthDataProvider,
        )
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        remoteMessagesDataSource: RemoteMessagesDataSource,
        localMessagesDataSource: LocalMessagesDataSource,
        remoteChatsDataSource: RemoteChatsDataSource,
        localChatsDataSource : LocalChatsDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): MessageRepository {
        return OfflineFirstMessageRepository(
            localMessagesDataSource = localMessagesDataSource,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteChatsDataSource: RemoteChatsDataSource,
        localChatsDataSource : LocalChatsDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ChatRepository {
        return OfflineFirstChatRepository(
            remoteChatsDataSource = remoteChatsDataSource,
            localChatsDataSource = localChatsDataSource,
            ioDispatcher = ioDispatcher,

        )
    }
}