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
package com.peterchege.pchat.core.work.sync_chats

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.peterchege.pchat.R
import com.peterchege.pchat.core.api.NetworkResult
import com.peterchege.pchat.domain.mappers.toEntity
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import com.peterchege.pchat.util.WorkConstants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlin.random.Random


@HiltWorker
class SyncChatsWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val localChatsDataSource: LocalChatsDataSource,
    private val remoteChatsDataSource: RemoteChatsDataSource,
    private val authRepository: AuthRepository,
):CoroutineWorker(appContext, workerParameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(appContext, WorkConstants.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Syncing")
                .build()

        )
    }

    override suspend fun doWork(): Result {
        val authUser = authRepository.getAuthUser()
            .filterNotNull()
            .first()
        val response = remoteChatsDataSource.getUserById(authUser.userId)
        when (response) {
            is NetworkResult.Success -> {
                if (response.data.chats != null) {
                    localChatsDataSource.clearChat()
                    localChatsDataSource.insertChats(chats = response.data.chats.map { it.toEntity() })
                    return Result.success()
                }else{
                    Result.retry()
                    return Result.failure()
                }
            }
            is NetworkResult.Error -> {
                Result.retry()
                return Result.failure()
            }

            is NetworkResult.Exception -> {
                Result.retry()
                return Result.failure()
            }
        }
    }
}