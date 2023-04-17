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
package com.peterchege.pchat.presentation.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.peterchege.pchat.domain.models.NetworkUser


@Composable
fun ChatItemCard(
    otherUserName: String,
    lastMessageText: String,
    lastMessageTimestamp:String,
    imageUrl: String,
    onChatClicked: (String) -> Unit,
    chatItem: NetworkUser,

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
                    text = lastMessageTimestamp,
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
