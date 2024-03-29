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
package com.peterchege.pchat.presentation.ui.screens.account_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.UiEvent
import com.peterchege.pchat.util.getGoogleSignInClient
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun AccountScreenPreview() {
    AccountScreen(navigateToSignInScreen = { })
}

@Composable
fun AccountScreen(
    navigateToSignInScreen: () -> Unit,
    viewModel: AccountScreenViewModel = hiltViewModel()
) {
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()

    AccountScreenContent(
        navigateToSignInScreen = navigateToSignInScreen,
        authUser = authUser,
        eventFlow = viewModel.eventFlow,
        clearChats = viewModel::clearChats
    )
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AccountScreenContent(
    navigateToSignInScreen: () -> Unit,
    authUser: NetworkUser?,
    eventFlow: SharedFlow<UiEvent>,
    clearChats: () -> Unit,
) {

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                SubcomposeAsyncImage(
                    model = authUser?.imageUrl ?: "",
                    loading = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(CircleShape)
                        .clickable {

                        },
                    contentDescription = "Profile Photo URL"
                )
                Spacer(modifier = Modifier.height(7.dp))
                authUser?.let { it1 ->
                    Text(
                        text = it1.fullName,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 19.sp
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = it1.email,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        val signInClient = getGoogleSignInClient(context = context)
                        signInClient.signOut()
                        clearChats()
                        navigateToSignInScreen()

                    }

                ) {
                    Text(text = "Sign Out")
                }
            }


        }
    }
}