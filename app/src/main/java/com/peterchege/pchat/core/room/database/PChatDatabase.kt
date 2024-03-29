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
package com.peterchege.pchat.core.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peterchege.pchat.core.room.dao.ChatDao
import com.peterchege.pchat.core.room.dao.MessageDao
import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.core.room.entities.MessageEntity

@Database(
    entities = [
        ChatEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class PChatDatabase : RoomDatabase() {
    abstract val chatDao: ChatDao

    abstract val messageDao:MessageDao
}