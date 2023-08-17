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
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.peterchege.pchat.core.api.PChatApi
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import com.peterchege.pchat.domain.repository.local.LocalMessagesDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteChatsDataSource
import com.peterchege.pchat.domain.repository.remote.RemoteMessagesDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncMessagesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams:WorkerParameters,
    private val api :PChatApi,
    private val remoteMessagesDataSource:RemoteMessagesDataSource,
    private val localMessagesDataSource:LocalMessagesDataSource,
    private val remoteChatsDataSource: RemoteChatsDataSource,
    private val localChatsDataSource: LocalChatsDataSource,
) :CoroutineWorker(context,workerParams){
    override suspend fun doWork(): Result {
        TODO()
    }


}