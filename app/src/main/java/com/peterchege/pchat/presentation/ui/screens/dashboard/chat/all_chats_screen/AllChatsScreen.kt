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
package com.peterchege.pchat.presentation.ui.screens.dashboard.chat.all_chats_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.peterchege.pchat.presentation.ui.components.ChatItemCard
import com.peterchege.pchat.util.Screens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AllChatsScreen(
    navController: NavController,
    viewModel: AllChatsScreenViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = viewModel.isError.value){
        if (viewModel.isError.value){
            scaffoldState.snackbarHostState.showSnackbar(
                message = viewModel.errorMsg.value
            )
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
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
        if (viewModel.isLoading.value){
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }else{
            if (viewModel.chats.value.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "No chats found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(items = viewModel.chats.value) { chat ->
                        ChatItemCard(
                            otherUserName = chat.fullName,
                            lastMessageText = "",
                            lastMessageTimestamp = "",
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

    }
}