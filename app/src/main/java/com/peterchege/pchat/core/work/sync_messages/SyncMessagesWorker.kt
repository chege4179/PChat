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
package com.peterchege.pchat.core.work.sync_messages

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.peterchege.pchat.R
import com.peterchege.pchat.core.api.NetworkResult
import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.local.LocalMessagesDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteMessagesDataSource
import com.peterchege.pchat.util.WorkConstants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

@HiltWorker
class SyncMessagesWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams:WorkerParameters,
    private val remoteMessagesDataSource:RemoteMessagesDataSource,
    private val localMessagesDataSource:LocalMessagesDataSource,
    private val authRepository: AuthRepository,
) :CoroutineWorker(context,workerParams){

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(context, WorkConstants.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Syncing")
                .build()

        )
    }
    override suspend fun doWork(): Result {
        val authUser = authRepository.getAuthUser()
            .filterNotNull()
            .first()
        val response  = remoteMessagesDataSource.getAllMessages(authUser.userId)
        when(response){
            is NetworkResult.Success -> {
                localMessagesDataSource.clearMessages()
                localMessagesDataSource.insertMessages(
                    messages = response.data.receivedMessages.map { it.toEntity() })
                localMessagesDataSource.insertMessages(
                    messages = response.data.sentMessages.map { it.toEntity() })
                return Result.success()
            }
            is NetworkResult.Error -> {
                return Result.failure()
            }
            is NetworkResult.Exception -> {
                return Result.failure()
            }
        }
    }
}