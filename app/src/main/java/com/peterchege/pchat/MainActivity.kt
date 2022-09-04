package com.peterchege.pchat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.peterchege.pchat.ui.theme.PChatTheme
import com.peterchege.pchat.util.Constants
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity  : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);

        val options = PusherOptions()
        options.setCluster("sa1");

        val pusher = Pusher(Constants.PUSHER_API_KEY, options)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")
            }

            override fun onError(message: String, code: String, e: Exception
            ) {
                Log.e("Pusher", "There was a problem connecting! code ($code), message ($message), exception($e)")
            }
        }, ConnectionState.ALL)

        val channel = pusher.subscribe("my-channel")
        channel.bind("my-event") { event ->
            Log.e("Pusher","Received event with data: ${event.data}")
        }

        setContent {
            PChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PChatTheme {
        Greeting("Android")
    }
}