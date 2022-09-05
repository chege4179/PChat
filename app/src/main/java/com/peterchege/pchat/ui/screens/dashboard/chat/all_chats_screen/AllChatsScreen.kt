package com.peterchege.pchat.ui.screens.dashboard.chat.all_chats_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.peterchege.pchat.ui.components.ChatCard
import com.peterchege.pchat.util.Screens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AllChatsScreen(
    navController: NavController,
    viewModel: AllChatsScreenViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.ADD_CHAT_SCREEN)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, "Add New Chat"
                )
            }
        }

    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(viewModel.chats.value) { chat ->
                ChatCard(
                    otherUserName = chat.name,
                    lastMessageText = chat.messages.last().message,
                    imageUrl = chat.imageUrl,
                    onChatClicked = {
                        navController.navigate(Screens.CHAT_SCREEN + "/${chat.userId}")

                    },
                    chatItem = chat
                )
            }
        }
    }
}