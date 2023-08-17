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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.presentation.ui.components.ChatItemCard
import com.peterchege.pchat.presentation.ui.components.LoadingComponent
import com.peterchege.pchat.util.Screens

@Composable
fun AllChatsScreen(
    navigateToAddChatScreen:() -> Unit,
    navigateToChatScreen:(String,String) -> Unit,
    viewModel: AllChatsScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isMessagesSyncing by viewModel.isMessagesSyncing.collectAsStateWithLifecycle()
    val isChatsSyncing by viewModel.isChatsSyncing.collectAsStateWithLifecycle()

    AllChatsScreenContent(
        navigateToAddChatScreen = navigateToAddChatScreen,
        navigateToChatScreen = navigateToChatScreen,
        startRefreshingChats = viewModel::startRefreshingChats,
        isChatsSyncing = isChatsSyncing,
        isMessagesSyncing = isMessagesSyncing,
        uiState = uiState
    )
}



@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AllChatsScreenContent(
    navigateToAddChatScreen:() -> Unit,
    navigateToChatScreen:(String,String) -> Unit,
    startRefreshingChats:() -> Unit,
    isChatsSyncing:Boolean,
    isMessagesSyncing:Boolean,
    uiState: AllChatsScreenUiState,


) {
    val scaffoldState = rememberScaffoldState()


    val pullRefreshState = rememberPullRefreshState(
        refreshing = isChatsSyncing || isMessagesSyncing,
        onRefresh = { startRefreshingChats() }
    )
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToAddChatScreen()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add New Chat"
                )
            }
        }

    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            PullRefreshIndicator(
                refreshing = isChatsSyncing || isMessagesSyncing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.Center)
            )
            when(uiState){
                is AllChatsScreenUiState.Error -> {

                }
                is AllChatsScreenUiState.Loading -> {
                    LoadingComponent()

                }
                is AllChatsScreenUiState.Success -> {
                    val chats = uiState.chats
                    if (chats.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "No chats found",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(items = chats) { chat ->
                                ChatItemCard(
                                    chatCardInfo = chat,
                                    onChatClicked = {
                                        navigateToChatScreen(chat.chatUserInfo.userId,chat.authUser.userId)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }


    }
}