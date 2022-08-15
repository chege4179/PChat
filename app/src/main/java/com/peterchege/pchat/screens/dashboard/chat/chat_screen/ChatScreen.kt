package com.peterchege.pchat.screens.dashboard.chat.chat_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ChatScreen(
    navController: NavController
){
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Chat Screen")

    }
}