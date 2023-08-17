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
package com.peterchege.pchat.core.datastore.repository

import com.peterchege.pchat.core.datastore.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultFCMProvider @Inject constructor(
    private val userPreferences: UserPreferences,

) {
    fun getFCMToken(): Flow<String?> {
        return userPreferences.getFCMToken()
    }

    suspend fun setFCMToken(token:String){
        return userPreferences.setFCMToken(token = token)
    }

}