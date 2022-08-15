package com.peterchege.pchat.screens.sign_in

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.peterchege.pchat.R
import com.peterchege.pchat.components.GoogleSignInButton
import com.peterchege.pchat.models.User
import com.peterchege.pchat.util.AuthResult
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens


@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel(),
){
    val context = LocalContext.current




    val authResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {


        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data).result
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            //viewModel.googleAuthForFirebase(account = account, navController = navController)
            viewModel.signWithCredential(credential = credential, navController = navController)


        } catch (e: ApiException) {
            Log.w("TAG", "Google sign in failed", e)
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
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(Constants.CLIENT_ID)
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    authResultLauncher.launch(googleSignInClient.signInIntent)

                }
            )
        }
    }

}



















