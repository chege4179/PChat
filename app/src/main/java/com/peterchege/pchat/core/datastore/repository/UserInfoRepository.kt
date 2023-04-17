package com.peterchege.pchat.core.datastore.repository

import android.content.Context
import androidx.datastore.dataStore
import com.peterchege.pchat.core.datastore.serializers.NetworkUserInfoSerializer
import com.peterchege.pchat.domain.models.NetworkUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


val Context.userDataStore by dataStore("user.json", NetworkUserInfoSerializer)


class UserInfoRepository @Inject constructor(
    @ApplicationContext private val context: Context
){

    fun getLoggedInUser(): Flow<NetworkUser?> {
        return context.userDataStore.data
    }
    suspend fun setLoggedInUser(user: NetworkUser) {
        context.userDataStore.updateData {
            user
        }
    }
    suspend fun unsetLoggedInUser() {
        context.userDataStore.updateData {
            null
        }
    }

}