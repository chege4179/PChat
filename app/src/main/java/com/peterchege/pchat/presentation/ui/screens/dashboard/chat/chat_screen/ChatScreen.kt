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
package com.peterchege.pchat.presentation.ui.screens.dashboard.chat.chat_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.peterchege.pchat.domain.mappers.toExternalModel
import com.peterchege.pchat.presentation.ui.components.LoadingComponent
import com.peterchege.pchat.presentation.ui.components.MessageCard
import com.peterchege.pchat.presentation.ui.components.MessageInput

@Composable
fun ChatScreen(
    viewModel: ChatScreenViewModel = hiltViewModel(),
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ChatScreenContent(
        uiState = uiState,
        message = viewModel.messageText.value,
        sendMessage = viewModel::sendMessage,
        onChangeMessageText = viewModel::onChangeMessageText
    )
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreenContent(
    uiState:ChatsScreenUiState,
    message:String,
    sendMessage:() -> Unit,
    onChangeMessageText:(String) -> Unit,

){
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()


    LaunchedEffect(key1 = true){
        if (uiState is ChatsScreenUiState.Success){
            if(uiState.chats.size > 1){
                scrollState.animateScrollToItem(index = uiState.chats.size - 1)
            }
        }


    }
    LaunchedEffect(key1 = true){
        if(uiState is ChatsScreenUiState.Error){
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiState.message
            )
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    if(uiState is ChatsScreenUiState.Success){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SubcomposeAsyncImage(
                                model = uiState.receiver.imageUrl,
                                loading = {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                    }
                                },
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(36.dp)
                                    .clip(CircleShape)
                                ,
                                contentDescription = "Profile Photo URL"
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(0.75f),
                                text = uiState.receiver.fullName
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(
                                    onClick = {

                                    }) {
                                    Icon(
                                        imageVector = Icons.Filled.Call,
                                        contentDescription = "Call",
                                        Modifier.size(26.dp)

                                    )
                                }
                                IconButton(
                                    onClick = {

                                    }) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "Settings",
                                        Modifier.size(26.dp)

                                    )
                                }
                            }
                        }
                    }else{
                        Text(
                            modifier = Modifier.fillMaxWidth(0.75f),
                            text = "P Chat User"
                        )
                    }
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }

    ) {
        when(uiState){
            is ChatsScreenUiState.Loading -> {
                LoadingComponent()
            }
            is ChatsScreenUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = uiState.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is ChatsScreenUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f),
                        state = scrollState
                    ){
                        items(items = uiState.chats){
                            MessageCard(
                                messageItem = it,
                                currentUserId = uiState.authUser.userId ,
                            )
                        }
                    }
                    MessageInput(
                        sendMessage = sendMessage,
                        onChangeMessageText = onChangeMessageText,
                        messageText = message,
                    )


                }
            }
        }




    }
}