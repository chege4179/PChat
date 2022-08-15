package com.peterchege.pchat.screens.dashboard

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter


@ExperimentalCoilApi
@Composable
fun DashBoardScreen(
    navController: NavController,
    viewModel: DashBoardViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            text = "PChat"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .clickable {
                                    viewModel.logoutUser(navController = navController)
                                }
                            ,
                            text = "Log Out"
                        )

                    }
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ) {
        Column() {
            Text("DashBoard")
            viewModel.displayName?.let {
                Text(it)
            }
            viewModel.imageUrl?.let {
                Image(
                    painter = rememberImagePainter(
                        data = "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1636811743/sample.jpg",
                        builder = {
                            crossfade(true)

                        }
                    ),
                    contentDescription = "Post Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }


        }



    }


}