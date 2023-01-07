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
package com.peterchege.pchat.domain.mappers

import com.peterchege.pchat.data.room.entities.ChatEntity
import com.peterchege.pchat.data.room.entities.MessageEntity
import com.peterchege.pchat.domain.models.ChatItem
import com.peterchege.pchat.domain.models.Message

fun ChatItem.toEntity():ChatEntity{
    return ChatEntity(
        name = name,
        chatId = id,
        googleId = userId,
        email = email,
        imageUrl = imageUrl,
    )
}

fun ChatEntity.asExternalModel(messages:List<Message>):ChatItem{
    return ChatItem(
        name = name,
        email = email,
        imageUrl = imageUrl,
        id = chatId,
        userId = googleId,
        messages = messages
    )
}

fun Message.toEntity():MessageEntity{
    return MessageEntity(
        sender = sender,
        receiver = receiver,
        sentAt = sentAt,
        sentOn = sentOn,
        message = message,
        messageId = id!!,
        isMine = isMine,
        isRead = isRead,
    )
}

fun MessageEntity.asExternalModel():Message{
    return Message(
        sender = sender,
        receiver = receiver,
        sentAt = sentAt,
        sentOn = sentOn,
        message = message,
        id = messageId,
        isMine = isMine,
        isRead = isRead,
    )
}