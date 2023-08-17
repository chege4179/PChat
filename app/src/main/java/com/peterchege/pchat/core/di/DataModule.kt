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
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.data.local.LocalChatsDataSourceImpl
import com.peterchege.pchat.data.local.LocalMessagesDataSourceImpl
import com.peterchege.pchat.data.remote.RemoteChatsDataSourceImpl
import com.peterchege.pchat.data.remote.RemoteMessagesDataSourceImpl
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.local.LocalMessagesDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteMessagesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {



    @Provides
    @Singleton
    fun provideLocalChatsDataSource(
        db:PChatDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): LocalChatsDataSource {
        return LocalChatsDataSourceImpl(
            db = db,
            ioDispatcher = ioDispatcher

        )

    }

    @Provides
    @Singleton
    fun provideLocalMessagesDataSource(
        db:PChatDatabase ,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): LocalMessagesDataSource {
        return LocalMessagesDataSourceImpl(db = db,ioDispatcher = ioDispatcher)

    }

    @Provides
    @Singleton
    fun provideRemoteMessagesDataSource(
        api:PChatApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RemoteMessagesDataSource {
        return RemoteMessagesDataSourceImpl(api = api,ioDispatcher = ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideRemoteChatsDataSource(
        api:PChatApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RemoteChatsDataSource {
        return RemoteChatsDataSourceImpl(api = api,ioDispatcher = ioDispatcher)
    }
}