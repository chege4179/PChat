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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.auth.FirebaseAuth
import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.core.datastore.repository.UserInfoRepository
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.data.local.LocalChatsDataSourceImpl
import com.peterchege.pchat.data.local.LocalUsersDataSourceImpl
import com.peterchege.pchat.data.remote.RemoteChatsDataSourceImpl
import com.peterchege.pchat.data.remote.RemoteUsersDataSourceImpl
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.local.LocalUserDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteUserDataSource
import com.peterchege.pchat.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideLocalUserDataSource(
        db:PChatDatabase,
        userInfoRepository:UserInfoRepository,
        auth:FirebaseAuth,
    ): LocalUserDataSource {
        return LocalUsersDataSourceImpl(
            db = db,
            userInfoRepository = userInfoRepository,
            auth = auth,
        )

    }

    @Provides
    @Singleton
    fun provideLocalChatsDataSource(db:PChatDatabase, ): LocalChatsDataSource {
        return LocalChatsDataSourceImpl(db = db)

    }

    @Provides
    @Singleton
    fun provideRemoteChatsDataSource(api:PChatApi): RemoteChatsDataSource {
        return RemoteChatsDataSourceImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideRemoteUserDataSource(api:PChatApi): RemoteUserDataSource {
        return RemoteUsersDataSourceImpl(api = api)
    }
}