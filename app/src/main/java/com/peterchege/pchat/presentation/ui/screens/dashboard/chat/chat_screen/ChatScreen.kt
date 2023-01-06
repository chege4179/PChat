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
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
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
    LaunchedEffect(viewModel.messages.value.size){
        if(viewModel.messages.value.size > 1){
            scrollState.animateScrollToItem(viewModel.messages.value.size - 1)
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
                                text = it.displayName
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
                        items(viewModel.messages.value){
                            MessageCard(

                                messageItem = it,
                                currentUser = viewModel.email!!
                            )
                        }
                    }
                    MessageInput()
                }
            }


        }



    }
}