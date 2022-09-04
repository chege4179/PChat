package com.peterchege.pchat.ui.screens.sign_in

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.peterchege.pchat.models.User
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,

):ViewModel() {
    private var _user = mutableStateOf<User?>(null)
    var user: State<User?> = _user

    private var _text = mutableStateOf("")
    var text:State<String> = _text

    fun onChangeUser(user:User,navController: NavController){
        val userInfoEditor = sharedPreferences.edit()
        userInfoEditor.apply{
            putString(Constants.USER_DISPLAY_NAME,user.displayName)
            putString(Constants.USER_ID,user.id)
            putString(Constants.USER_EMAIL,user.email)
            putString(Constants.USER_IMAGE_URL,user.imageUrl)
//            putString(Constants.FCM_TOKEN,token)
            apply()
        }
        navController.navigate(Screens.DASHBOARD_SCREEN)
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val signInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(Constants.CLIENT_ID)
            .requestId()
            .requestProfile()
            .build()

        return GoogleSignIn.getClient(context, signInOption)
    }



}