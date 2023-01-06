package com.peterchege.pchat.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.peterchege.pchat.data.api.PChatApi
import com.peterchege.pchat.data.room.database.PChatDatabase
import com.peterchege.pchat.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePChatApi(): PChatApi {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PChatApi::class.java)
    }
    @Provides
    @Singleton
    fun provideSharedPreference(app: Application): SharedPreferences {
        return app.getSharedPreferences("user", Context.MODE_PRIVATE)
    }

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