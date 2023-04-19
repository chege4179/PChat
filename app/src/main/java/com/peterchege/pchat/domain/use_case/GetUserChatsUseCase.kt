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

import com.peterchege.pchat.core.room.entities.ChatEntity
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.domain.repository.ChatRepository
import com.peterchege.pchat.presentation.models.ChatCardInfo
import com.peterchege.pchat.presentation.models.ChatWithSender
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserChatsUseCase @Inject constructor(
    private val getAuthUser: GetAuthUserUseCase,

    private val offlineFirstChatRepository: ChatRepository,
) {

    operator fun invoke(): Flow<List<ChatCardInfo?>> {
        val user = getAuthUser()
        return offlineFirstChatRepository.getChats()
            .mapToChatWithAuthUser(user)
            .map { chatWithSenders ->
                return@map chatWithSenders.map { chatWithSender ->
                    val lastMessage =
                        offlineFirstChatRepository.getLastMessage(receiverId = chatWithSender.authUser?.userId ?:"")
                            .map { it?.toExternalModel() }
                    return@map chatWithSender.authUser?.let {
                        ChatCardInfo(
                            authUser = it,
                            chatUserInfo = chatWithSender.chatUserInfo,
                            lastMessage = lastMessage,
                        )
                    }

                }
            }

    }
}


private fun Flow<List<ChatEntity>>.mapToChatWithAuthUser(authUser: Flow<NetworkUser?>):
        Flow<List<ChatWithSender>> =
    combine(authUser) { chats, user ->
        chats.map {
            ChatWithSender(
                chatUserInfo = it.toExternalModel(),
                authUser = user
            )
        }
    }