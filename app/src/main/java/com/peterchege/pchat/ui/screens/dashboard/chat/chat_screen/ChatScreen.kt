package com.peterchege.pchat.ui.screens.dashboard.chat.chat_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.peterchege.pchat.ui.components.MessageInput

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatScreenViewModel = hiltViewModel(),
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                                modifier = Modifier.fillMaxWidth(0.5f),
                                text = it.displayName
                            )
                            Spacer(modifier = Modifier.width(50.dp))
                        }
                    }

                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }

    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(5.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f)
            ){



            }
            MessageInput()
        }



    }
}