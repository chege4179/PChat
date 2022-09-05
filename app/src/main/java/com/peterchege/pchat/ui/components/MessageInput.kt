package com.peterchege.pchat.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peterchege.pchat.R
import com.peterchege.pchat.ui.screens.dashboard.chat.chat_screen.ChatScreenViewModel

@Composable
fun MessageInput(
    viewModel: ChatScreenViewModel = hiltViewModel() // 1
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        TextField(
            // 4
            modifier = Modifier.weight(1f),
            value = viewModel.messageText.value,
            onValueChange = {
                viewModel.onChangeMessageText(it)
            },
            keyboardActions = KeyboardActions { viewModel.sendMessage() },
        )
        Button(
            // 5
            modifier = Modifier
                .height(45.dp)
                .clip(CircleShape),
            onClick = { viewModel.sendMessage() },
            enabled = viewModel.messageText.value.isNotBlank(),
        ) {
            Icon( // 6
                imageVector = Icons.Default.Send,
                contentDescription = "Send Button"
            )

        }
    }
}