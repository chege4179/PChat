package com.peterchege.pchat.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.peterchege.pchat.api.PChatApi
import com.peterchege.pchat.room.database.PChatDatabase
import com.peterchege.pchat.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePChatApi():PChatApi{
        var okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

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