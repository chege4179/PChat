package com.peterchege.pchat.screens.addchat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun AddChatScreen(
    navController: NavController
){
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Add Chat Screen")

    }
}