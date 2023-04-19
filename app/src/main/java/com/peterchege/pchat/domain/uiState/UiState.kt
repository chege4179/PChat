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
package com.peterchege.pchat.domain.uiState

import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.models.NetworkUser
import com.skydoves.sealedx.core.Extensive
import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed


@ExtensiveSealed(
    models = [
        ExtensiveModel(AddChat::class),
        ExtensiveModel(AllChats::class),
        ExtensiveModel(Messages::class),
        ExtensiveModel(Receiver::class),
        ExtensiveModel(Sender::class),

    ]
)
sealed interface UiState {
    data class Idle (val message:String):UiState
    data class Success(val data: Extensive) : UiState
    data class Loading(val message:String) : UiState
    data class Error (val errorMessage:String): UiState
}

data class AddChat(
    val searchUsers:List<NetworkUser>
)

data class AllChats(
    val chats:List<NetworkUser>
)

data class Receiver(
    val user:NetworkUser
)

data class Sender(
    val user:NetworkUser,
)

data class Messages(
    val messages:List<Message>
)