package com.peterchege.pchat.ui.screens.addchat

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.pchat.ui.components.ProfileCard
import com.peterchege.pchat.ui.theme.MainWhiteColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddChatScreen(
    navController: NavController,
    viewModel:AddChatScreenViewModel = hiltViewModel()
){
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(5.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()

        ) {
            TextField(
                value = viewModel.searchTerm.value,
                onValueChange = {
                    viewModel.onChangeSearchTerm(it)
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
                    disabledTextColor = MainWhiteColor,
                    backgroundColor = MainWhiteColor,
                    focusedIndicatorColor = MainWhiteColor,
                    unfocusedIndicatorColor = MainWhiteColor,
                    disabledIndicatorColor = MainWhiteColor
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
            if (viewModel.isLoading.value){
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }else{
                if (viewModel.searchUser.value.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No users found")
                    }
                }else{
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp)
                    ) {
                        items(viewModel.searchUser.value) { user ->
                            ProfileCard(
                                navController = navController,
                                user =user,
                                onProfileNavigate = {
                                    viewModel.onProfileNavigate(it,navController = navController)
                                }
                            )
                        }
                    }
                }
            }


        }
    }
}