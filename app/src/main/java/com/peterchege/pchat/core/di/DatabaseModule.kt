package com.peterchege.pchat.core.di

import android.app.Application
import androidx.room.Room
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePChatDatabase(app: Application): PChatDatabase {
        return Room.databaseBuilder(
            app,
            PChatDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }
}