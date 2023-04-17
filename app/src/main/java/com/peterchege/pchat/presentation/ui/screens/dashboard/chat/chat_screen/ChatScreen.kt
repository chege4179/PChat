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
import com.peterchege.pchat.presentation.ui.components.MessageCard
import com.peterchege.pchat.presentation.ui.components.MessageInput

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatScreenViewModel = hiltViewModel(),
){
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    val authUser = viewModel.authUser.collectAsStateWithLifecycle().value
    val messages = viewModel.messages.value
        .collectAsStateWithLifecycle(initialValue = emptyList())
        .value
        .map { it.toExternalModel() }

    LaunchedEffect(key1 = messages.size){
        if(messages.size > 1){
            scrollState.animateScrollToItem(index = messages.size - 1)
        }

    }
    LaunchedEffect(key1 = viewModel.isError.value){
        if(viewModel.isError.value){
            scaffoldState.snackbarHostState.showSnackbar(
                message = viewModel.errorMsg.value
            )
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    viewModel.activeChatUser.value?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SubcomposeAsyncImage(
                                model = it.imageUrl,
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
                                text = it.fullName
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(
                                    onClick = {

                                    }) {
                                    Icon(
                                        Icons.Filled.Call,
                                        contentDescription = "Call",
                                        Modifier.size(26.dp)

                                    )
                                }
                                IconButton(
                                    onClick = {

                                    }) {
                                    Icon(
                                        Icons.Filled.Settings,
                                        contentDescription = "Settings",
                                        Modifier.size(26.dp)

                                    )
                                }

                            }
                        }
                    }

                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            if(viewModel.isError.value){
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = viewModel.errorMsg.value,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }else{
                if (viewModel.isLoading.value){
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }else{
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f),
                        state = scrollState,

                        ){
                        items(items = messages){
                            MessageCard(
                                messageItem = it,
                                currentUserId = authUser?.userId ?: "",
                            )
                        }
                    }
                    MessageInput()
                }
            }


        }



    }
}