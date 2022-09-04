package com.peterchege.pchat.ui.screens.dashboard.chat.chat_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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