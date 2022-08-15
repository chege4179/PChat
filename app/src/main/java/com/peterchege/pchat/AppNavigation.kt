package com.peterchege.pchat

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterchege.pchat.screens.addchat.AddChatScreen
import com.peterchege.pchat.screens.dashboard.DashBoardScreen
import com.peterchege.pchat.screens.dashboard.chat.chat_screen.ChatScreen
import com.peterchege.pchat.screens.sign_in.SignInScreen
import com.peterchege.pchat.util.Screens


@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AppNavigationViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = viewModel.getInitialRoute()
    ){
        composable(Screens.SIGN_IN_SCREEN){
            SignInScreen(navController = navController)
        }
        composable(Screens.DASHBOARD_SCREEN){
            DashBoardScreen(navController = navController)
        }
        composable(Screens.ADD_CHAT_SCREEN){
            AddChatScreen(navController = navController)
        }
        composable(Screens.CHAT_SCREEN + "/{chatId}"){
            ChatScreen(navController = navController)
        }
    }
}