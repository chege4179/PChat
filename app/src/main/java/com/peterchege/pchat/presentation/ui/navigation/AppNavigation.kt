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
package com.peterchege.pchat.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterchege.pchat.presentation.ui.screens.account_screen.AccountScreen
import com.peterchege.pchat.presentation.ui.screens.add_chat_screen.AddChatScreen
import com.peterchege.pchat.presentation.ui.screens.dashboard.DashBoardScreen
import com.peterchege.pchat.presentation.ui.screens.dashboard.chat.chat_screen.ChatScreen
import com.peterchege.pchat.presentation.ui.screens.sign_in.SignInScreen
import com.peterchege.pchat.util.Screens


@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AppNavigationViewModel = hiltViewModel()
) {

    val user = viewModel.user.collectAsStateWithLifecycle()
    fun getInitialRoute(): String {
        return if (user.value == null) {
            Screens.SIGN_IN_SCREEN
        } else {
            if (user.value?.email == "") {
                Screens.SIGN_IN_SCREEN
            } else {
                Screens.DASHBOARD_SCREEN
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = getInitialRoute()
    ) {
        composable(route = Screens.SIGN_IN_SCREEN) {
            SignInScreen(navigateToDashBoardScreen = navController::navigateToDashboardScreen)
        }
        composable(route = Screens.DASHBOARD_SCREEN) {
            DashBoardScreen(
                navigateToAddChatScreen = navController::navigateToAddChatScreen,
                navigateToAccountScreen = navController::navigateToAccountScreen,
                navigateToChatScreen = navController::navigateToChatScreen,
            )
        }
        composable(route = Screens.ADD_CHAT_SCREEN) {
            AddChatScreen(
                navigateToChatScreen = navController::navigateToChatScreen,
            )
        }
        composable(route = Screens.CHAT_SCREEN + "/{receiverId}/{senderId}") {
            ChatScreen()
        }
        composable(route = Screens.ACCOUNT_SCREEN) {
            AccountScreen(
                navigateToSignInScreen = navController::navigateToSignInScreen
            )
        }
    }
}


fun NavController.navigateToSignInScreen(){
    navigate(route = Screens.SIGN_IN_SCREEN)
}
fun NavController.navigateToDashboardScreen(){
    navigate(route = Screens.DASHBOARD_SCREEN)
}

fun NavController.navigateToAddChatScreen(){
    navigate(route = Screens.ADD_CHAT_SCREEN)
}

fun NavController.navigateToChatScreen(receiverId:String,senderId:String){
    navigate(route = Screens.CHAT_SCREEN + "/${receiverId}/${senderId}")
}

fun NavController.navigateToAccountScreen(){
    navigate(route = Screens.ACCOUNT_SCREEN)
}