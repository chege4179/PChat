package com.peterchege.pchat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.pchat.models.User

@ExperimentalCoilApi
@Composable
fun ProfileCard(
    navController: NavController,
    onProfileNavigate:(String) ->Unit,
    user: User,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(70.dp)
            .clickable {
                onProfileNavigate(user.userId)
            }
        ,
        shape = RoundedCornerShape(15),
        elevation = 3.dp
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .clip(CircleShape)
                ,
                painter = rememberImagePainter(
                    data = user.imageUrl,
                    builder = {
                        crossfade(true)

                    },
                ),
                contentDescription = "")
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.displayName,
                    fontWeight = FontWeight.Bold,
                    )
                Text(text=user.email)
            }
        }
    }
}