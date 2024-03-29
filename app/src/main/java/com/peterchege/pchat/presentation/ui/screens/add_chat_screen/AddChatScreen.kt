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
package com.peterchege.pchat.presentation.ui.screens.add_chat_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.presentation.ui.components.ProfileCard


@Composable
fun AddChatScreen(
    navigateToChatScreen:(String,String) -> Unit,
    viewModel:AddChatScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()


    AddChatScreenContent(
        navigateToChatScreen = navigateToChatScreen,
        uiState =uiState ,
        authUser = authUser,
        searchTerm = viewModel.searchTerm.value,
        onChangeSearchTerm = viewModel::onChangeSearchTerm
    )
}
@Preview
@Composable
fun AddChatScreenPreview(){
    AddChatScreenContent(
        navigateToChatScreen = { it1,it2 ->  },
        uiState = AddChatUiState.ResultsFound(users = emptyList()),
        authUser = null,
        searchTerm = "",
        onChangeSearchTerm = {  }
    )
}
@OptIn(ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddChatScreenContent(
    navigateToChatScreen:(String,String) -> Unit,
    uiState:AddChatUiState,
    authUser:NetworkUser?,
    searchTerm:String,
    onChangeSearchTerm:(String) -> Unit,
){

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()

        ) {
            TextField(
                value = searchTerm,
                onValueChange = {
                    onChangeSearchTerm(it)
                },
                placeholder = {
                    Text(
                        text = "Search",
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .clickable {
                    },
                shape = RoundedCornerShape(size = 8.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    disabledTextColor = Color.LightGray,
                    backgroundColor = Color.LightGray,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    disabledIndicatorColor = Color.LightGray
                ),
                textStyle = TextStyle(color = Color.Black),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Product",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {

                            }
                    )
                }
            )
            when(uiState){
                is AddChatUiState.Idle -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Search Users"
                        )
                    }
                }
                is AddChatUiState.ResultsFound -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp)
                    ) {
                        items(items = uiState.users) { user ->
                            ProfileCard(
                                user =user,
                                onProfileNavigate = {
                                    navigateToChatScreen(it,authUser!!.userId)
                                }
                            )
                        }
                    }
                }
                is AddChatUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is AddChatUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.message
                        )
                    }
                }
            }
        }
    }
}