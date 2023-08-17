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
package com.peterchege.pchat.presentation.ui.screens.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter

import com.peterchege.pchat.presentation.ui.screens.dashboard.chat.all_chats_screen.AllChatsScreen
import com.peterchege.pchat.presentation.ui.screens.dashboard.status.all_status_screen.AllStatusScreen
import com.peterchege.pchat.presentation.ui.theme.testColor
import com.peterchege.pchat.util.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun DashBoardScreen(
    navController: NavController,
    viewModel: DashBoardViewModel = hiltViewModel(),
) {
    val authUser = viewModel.authUser.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
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
                                .width(36.dp)
                                .height(36.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navController.navigate(Screens.ACCOUNT_SCREEN)
                                }
                            ,
                            contentDescription = "Profile Photo URL"
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            text = "PChat"
                        )
                        Spacer(modifier = Modifier.width(50.dp))
                    }
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 2 },
        )
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                navController = navController

            )
        }

    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf("Chats","Status")
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.White,
        divider = {
            TabRowDefaults.Divider(
                thickness = 2.dp,
                color = Color.White
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(

                height = 2.dp,
                color = testColor
            )
        }
    ) {
        list.forEachIndexed { index, _->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) testColor else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(pagerState: PagerState, navController: NavController) {
    HorizontalPager(
        modifier = Modifier,
        state = pagerState,
        pageSpacing = 0.dp,
        userScrollEnabled = true,
        reverseLayout = false,
        contentPadding = PaddingValues(0.dp),
        beyondBoundsPageCount = 0,
        pageSize = PageSize.Fill,
        key = null,
    ){
        when (pagerState.currentPage) {
            0 -> AllChatsScreen(navController = navController)
            1 -> AllStatusScreen(navController = navController)

        }
    }
}