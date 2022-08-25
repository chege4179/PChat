package com.peterchege.pchat.screens.sign_in

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.peterchege.pchat.R
import com.peterchege.pchat.components.GoogleSignInButton
import com.peterchege.pchat.models.User

import com.peterchege.pchat.util.AuthResultContract

import com.peterchege.pchat.util.Screens
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            try {
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {

                } else {
                    coroutineScope.launch {
                        Log.e("Email",account.email?:"No email")
                        Log.e("Name",account.displayName?:"No displayName")
                        account.let {
                            val signInUser = User(
                                displayName = it.displayName!!,
                                email = it.email!!,
                                id = it.id!!,
                                imageUrl = it.photoUrl.toString()
                            )
                            viewModel.onChangeUser(user = signInUser,navController = navController)
                        }
                    }
                }
            } catch (e: ApiException) {
                Log.e("Api Exception",e.localizedMessage?: "API ERROR")
            }
        }


    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GoogleSignInButton(
                text = "Sign In with Google",
                icon = painterResource(id = R.drawable.ic_google_sign_in_button),
                loadingText = "Signing In...",
                isLoading = false,
                onClick = {
                    val signInClient = viewModel.getGoogleSignInClient(context=context)

                    authResultLauncher.launch(1)

                }
            )
        }
    }
}



















