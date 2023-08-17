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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.pchat.domain.models.NetworkUser
import com.peterchege.pchat.presentation.ui.screens.dashboard.chat.all_chats_screen.AllChatsScreen
import com.peterchege.pchat.presentation.ui.screens.dashboard.status.all_status_screen.AllStatusScreen
import kotlinx.coroutines.launch


@Preview
@Composable
fun DashBoardScreenContentPreview(){
    DashBoardScreenContent(
        navigateToAddChatScreen = { },
        navigateToChatScreen = { it1,it2 ->  },
        navigateToAccountScreen = {  },
        isChatsSyncing = false,
        isMessagesSyncing =false ,
        authUser = null,
        startRefreshingChats = {  }
    )
}



@Composable
fun DashBoardScreen(
    navigateToAddChatScreen:() -> Unit,
    navigateToChatScreen:(String,String) -> Unit,
    navigateToAccountScreen:() -> Unit,
    viewModel: DashBoardViewModel = hiltViewModel(),
){
    val isMessagesSyncing by viewModel.isMessagesSyncing.collectAsStateWithLifecycle()
    val isChatsSyncing by viewModel.isChatsSyncing.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()


    DashBoardScreenContent(
        navigateToAddChatScreen = navigateToAddChatScreen,
        navigateToChatScreen = navigateToChatScreen,
        navigateToAccountScreen = navigateToAccountScreen,
        isChatsSyncing = isChatsSyncing,
        isMessagesSyncing =isMessagesSyncing ,
        authUser = authUser,
        startRefreshingChats = viewModel::startRefreshingChats
    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun DashBoardScreenContent(
    navigateToAddChatScreen:() -> Unit,
    navigateToChatScreen:(String,String) -> Unit,
    navigateToAccountScreen:() -> Unit,
    isChatsSyncing:Boolean,
    isMessagesSyncing:Boolean,
    authUser:NetworkUser?,
    startRefreshingChats:() -> Unit,
) {

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
                                    navigateToAccountScreen()
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
        Column(modifier = Modifier.background(Color.White)
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                navigateToChatScreen = navigateToChatScreen,
                navigateToAddChatScreen = navigateToAddChatScreen
            )
        }

    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState,) {
    val list = listOf("Chats","Status")
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
    ) {
        list.forEachIndexed { index, _->
            Tab(
                text = {
                    Text(
                        text = list[index],
                        style = MaterialTheme.typography.body1
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
fun TabsContent(
    pagerState: PagerState,
    navigateToAddChatScreen:() -> Unit,
    navigateToChatScreen:(String,String) -> Unit,
) {
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
            0 -> AllChatsScreen(
                navigateToAddChatScreen = navigateToAddChatScreen,
                navigateToChatScreen = navigateToChatScreen
            )
            1 -> AllStatusScreen()

        }
    }
}



//@Composable
//fun TextTabs() {
//    var state by remember { mutableStateOf(0) }
//    val titles = listOf("TAB 1", "TAB 2", "TAB 3 WITH LOTS OF TEXT")
//    Column {
//        TabRow(selectedTabIndex = state) {
//            titles.forEachIndexed { index, title ->
//                Tab(
//                    text = { Text(title) },
//                    selected = state == index,
//                    onClick = { state = index }
//                )
//            }
//        }
//        Text(
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            text = "Text tab ${state + 1} selected",
//            style = MaterialTheme.typography.body1
//        )
//    }
//}