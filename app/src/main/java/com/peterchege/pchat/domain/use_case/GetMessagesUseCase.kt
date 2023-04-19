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
package com.peterchege.pchat.domain.use_case

import com.peterchege.pchat.data.OfflineFirstChatRepository
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val offlineFirstChatRepository: ChatRepository
) {

    operator fun invoke(senderId:String,receiverId:String): Flow<List<Message>> {
        return offlineFirstChatRepository
            .getChatMessagesBetween2Users(senderId = senderId,receiverId = receiverId)
            .map { it.map { messageEntity ->  messageEntity.toExternalModel() } }

    }
}
