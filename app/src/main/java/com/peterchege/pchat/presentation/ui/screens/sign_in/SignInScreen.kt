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
package com.peterchege.pchat.presentation.ui.screens.sign_in

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.peterchege.pchat.R
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.presentation.ui.components.GoogleSignInButton

import com.peterchege.pchat.util.AuthResultContract
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    navigateToDashBoardScreen: () -> Unit,
    viewModel: SignInScreenViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()


    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            try {
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    Toast.makeText(
                        context,
                        "Your devices need google play services to login using google",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    coroutineScope.launch {

                        account.let {
                            val signInUser = User(
                                displayName = it.displayName!!,
                                email = it.email!!,
                                userId = it.id!!,
                                imageUrl = it.photoUrl.toString()
                            )
                            viewModel.onChangeUser(user = signInUser)
                        }
                    }
                }
            } catch (e: ApiException) {

            }
        }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navigateToDashBoardScreen()
                }

                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GoogleSignInButton(
                text = "Sign In with Google",
                icon = painterResource(id = R.drawable.ic_google_sign_in_button),
                loadingText = "Signing In...",
                isLoading = false,
                onClick = {
                    authResultLauncher.launch( 1)
                }
            )
        }
    }
}



















