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