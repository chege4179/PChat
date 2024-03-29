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
package com.peterchege.pchat.core.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import com.peterchege.pchat.R
import com.peterchege.pchat.data.OfflineFirstMessageRepository
import com.peterchege.pchat.domain.models.Message
import com.peterchege.pchat.domain.repository.AuthRepository
import com.peterchege.pchat.presentation.MainActivity
import com.peterchege.pchat.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService  : FirebaseMessagingService() {

    @Inject
    lateinit var json:Json

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var offlineFirstMessageRepository: OfflineFirstMessageRepository

    companion object {
        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }

    override  fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
            token = newToken

    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val message = Message(
                messageId = remoteMessage.data["messageId"] ?:"",
                senderId = remoteMessage.data["senderId"] ?:"",
                receiverId = remoteMessage.data["receiverId"] ?:"",
                message = remoteMessage.data["message"] ?:"",
                sentAt = remoteMessage.data["sentAt"] ?:"",
                isRead = false,
            )
            CoroutineScope(Dispatchers.IO).launch {
                offlineFirstMessageRepository.insertMessage(message = message)
            }


        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            sendNotification(it.body!!,it.title!!)
        }
    }

    private fun sendNotification(messageBody: String,messageTitle:String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}

