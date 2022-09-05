package com.peterchege.pchat.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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