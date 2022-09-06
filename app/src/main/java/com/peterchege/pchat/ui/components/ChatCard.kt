package com.peterchege.pchat.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.peterchege.pchat.R
import com.peterchege.pchat.models.ChatItem
import com.peterchege.pchat.models.User

@Composable
fun ChatCard(
    otherUserName: String,
    lastMessageText: String,
    imageUrl: String,
    onChatClicked: (String) -> Unit,
    chatItem: ChatItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onChatClicked(chatItem.userId)
            }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .clip(CircleShape)
                    ,
                    painter = rememberImagePainter(
                        data = imageUrl,
                        builder = {
                            crossfade(true)

                        },
                    ),
                    contentDescription = "")
                Text(
                    text = otherUserName,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 16.dp)
                )
                //displays last message
                Text(
                    text = lastMessageText,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

        }
    }
}

@Composable
fun ChatItemCard(
    otherUserName: String,
    lastMessageText: String,
    imageUrl: String,
    onChatClicked: (String) -> Unit,
    chatItem: ChatItem
) {
    Row(
        Modifier
            .clickable {
                onChatClicked(chatItem.userId)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)

    ) {
        Image(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(CircleShape)
            ,
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)

                },
            ),
            contentDescription = "")
        Spacer(modifier = Modifier.width(8.dp))
        Column(Modifier.padding(horizontal = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = otherUserName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = chatItem.messages.last().sentAt,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
            Text(
                text = lastMessageText,
                maxLines = 1,
                fontSize = 15.sp,
                color = Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                color = Color(0xFFebebeb),
                thickness = 2.dp
            )
        }
    }
}
