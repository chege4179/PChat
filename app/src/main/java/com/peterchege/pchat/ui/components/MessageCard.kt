package com.peterchege.pchat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterchege.pchat.models.Message

@Composable
fun MessageCard(messageItem: Message,currentUser:String) { // 1
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = when { // 2
            currentUser == messageItem.sender -> Alignment.End
            else -> Alignment.Start
//            messageItem.isMine -> Alignment.End
//            else -> Alignment.Start
        },
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = cardShapeFor(message = messageItem, currentUser = currentUser), // 3
            backgroundColor = when {
                currentUser == messageItem.sender -> MaterialTheme.colors.primary
                else -> MaterialTheme.colors.secondary
//                messageItem.isMine -> MaterialTheme.colors.primary
//                else -> MaterialTheme.colors.secondary
            },
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = messageItem.message,
                color = when {
                    currentUser == messageItem.sender -> MaterialTheme.colors.onPrimary
                    else -> MaterialTheme.colors.onSecondary
                },
            )
        }
        Text( // 4
            text = messageItem.sender,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun cardShapeFor(message: Message,currentUser: String): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return when {
        currentUser == message.sender -> roundedCorners.copy(bottomEnd = CornerSize(0))
        else -> roundedCorners.copy(bottomStart = CornerSize(0))
    }
}