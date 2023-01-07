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
package com.peterchege.pchat.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peterchege.pchat.presentation.ui.screens.dashboard.chat.chat_screen.ChatScreenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessageInput(
    viewModel: ChatScreenViewModel = hiltViewModel() // 1
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,

        ) {

        OutlinedTextField(
            value = viewModel.messageText.value,
            onValueChange = {
                viewModel.onChangeMessageText(it)
            },
            singleLine = false,
            maxLines = 5,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth(0.85f)
                .weight(1f)
                .height(50.dp),

        )
        Spacer(modifier = Modifier.width(5.dp))
        Button(
            // 5
            modifier = Modifier
                .height(45.dp)
                .width(45.dp)
                .clip(CircleShape),
            shape = RoundedCornerShape(20.dp),
            onClick = { viewModel.sendMessage() },
            enabled = viewModel.messageText.value.isNotBlank(),
        ) {
            Icon( // 6
                modifier = Modifier.size(35.dp),
                imageVector = Icons.Default.Send,
                contentDescription = "Send Button"
            )

        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@Preview
@Composable
fun MessageInputPreview() {
    MessageInputDummy()
}


@Composable
fun MessageInputDummy(

) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        OutlinedTextField(
            // 4
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .weight(1f)
                .height(45.dp),
            shape = RoundedCornerShape(20.dp),
            value = "",
            onValueChange = {


            },
            keyboardActions = KeyboardActions {

            },
        )
        Spacer(modifier = Modifier.width(5.dp))
        Button(

            // 5
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(CircleShape),
            onClick = { },
            shape = RoundedCornerShape(20.dp),
            enabled = true,
        ) {
            Icon( // 6
                imageVector = Icons.Default.Send,
                contentDescription = "Send Button",
                Modifier.size(35.dp)
            )

        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}

