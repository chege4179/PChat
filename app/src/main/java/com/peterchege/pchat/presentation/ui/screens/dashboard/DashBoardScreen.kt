package com.peterchege.pchat.presentation.ui.screens.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import com.peterchege.pchat.R
import com.peterchege.pchat.presentation.ui.screens.dashboard.chat.all_chats_screen.AllChatsScreen
import com.peterchege.pchat.presentation.ui.screens.dashboard.status.all_status_screen.AllStatusScreen
import com.peterchege.pchat.presentation.ui.theme.testColor
import com.peterchege.pchat.util.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun DashBoardScreen(
    navController: NavController,
    viewModel: DashBoardViewModel = hiltViewModel(),
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
                            model = viewModel.imageUrl,
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
                                .clickable{
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
        val pagerState = rememberPagerState()
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



@ExperimentalPagerApi
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
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
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

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, navController: NavController) {
    HorizontalPager(state = pagerState ,count =2) { page ->
        when(page) {
            0 -> AllChatsScreen(navController = navController)
            1 -> AllStatusScreen(navController = navController)

        }
    }
}