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