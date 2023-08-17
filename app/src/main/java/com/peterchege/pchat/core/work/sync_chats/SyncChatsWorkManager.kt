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
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.peterchege.pchat.core.work.sync_messages.SyncMessagesWorker
import com.peterchege.pchat.util.WorkConstants
import com.peterchege.pchat.util.anyRunning
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

interface SyncChatsWorkManager {
    val isSyncing: Flow<Boolean>


    fun startSyncChats()
}


class SyncChatsWorkManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) :SyncChatsWorkManager{

    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(WorkConstants.syncChatsWorker)
    .map(MutableList<WorkInfo>::anyRunning)
    .asFlow()
    .conflate()


    override fun startSyncChats() {
        val syncChatsRequest = OneTimeWorkRequestBuilder<SyncChatsWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .build()
            )
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.beginUniqueWork(
            WorkConstants.syncMessagesWorker,
            ExistingWorkPolicy.KEEP,
            syncChatsRequest
        )
            .enqueue()
    }



}