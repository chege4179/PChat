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
package com.peterchege.pchat.data.local

import com.peterchege.pchat.core.di.IoDispatcher
import com.peterchege.pchat.core.room.database.PChatDatabase
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.domain.repository.local.LocalChatsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalChatsDataSourceImpl @Inject constructor(
    private val db:PChatDatabase,
    @IoDispatcher private val ioDispatcher:CoroutineDispatcher
):LocalChatsDataSource {
    override fun getLocalChats(): Flow<List<ChatEntity>> {
        return db.chatDao.getLocalChats().flowOn(ioDispatcher)
    }

    override fun getChatUserById(userId: String): Flow<ChatEntity?> {
        return db.chatDao.getChatUserById(userId = userId).flowOn(ioDispatcher)
    }

    override suspend fun insertChats(chats: List<ChatEntity>) {
        withContext(ioDispatcher){
            db.chatDao.insertChats(chats = chats)
        }
    }

    override suspend fun deleteChatById(userId: String) {
        withContext(ioDispatcher){
            db.chatDao.deleteChatById(userId = userId)
        }
    }

    override suspend fun clearChat() {
        withContext(ioDispatcher){
            db.chatDao.clearChats()
        }
    }

}