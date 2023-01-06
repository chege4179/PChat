package com.peterchege.pchat.presentation.ui.screens.sign_in

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.common.api.ApiException
import com.peterchege.pchat.R
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.presentation.ui.components.GoogleSignInButton

import com.peterchege.pchat.util.AuthResultContract

import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                                userId = it.id!!,
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



















