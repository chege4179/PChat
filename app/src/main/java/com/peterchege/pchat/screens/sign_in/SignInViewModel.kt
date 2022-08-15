package com.peterchege.pchat.screens.sign_in

import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.peterchege.pchat.messaging.MyFirebaseMessagingService.Companion.token
import com.peterchege.pchat.models.User
import com.peterchege.pchat.util.AuthResult
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,

):ViewModel() {
    lateinit var auth: FirebaseAuth

    private var _user = mutableStateOf<User?>(null)
    var user: State<User?> = _user


    fun onChangeUser(user:User,navController: NavController){
        val userInfoEditor = sharedPreferences.edit()
        userInfoEditor.apply{
            putString(Constants.USER_DISPLAY_NAME,user.displayName)
            putString(Constants.USER_ID,user.id)
            putString(Constants.USER_EMAIL,user.email)
            putString(Constants.USER_IMAGE_URL,user.imageUrl)
            putString(Constants.FCM_TOKEN,token)
            apply()
        }
        navController.navigate(Screens.DASHBOARD_SCREEN)
    }

    private var _text = mutableStateOf("")
    var text:State<String> = _text

    fun signInWithGoogle(){



    }
    fun signWithCredential(credential: AuthCredential,navController: NavController) = viewModelScope.launch {
        try {
            val response = Firebase.auth.signInWithCredential(credential)
            val account = response.result.user
            if (account != null) {
                onChangeUser(User(
                    displayName = account.displayName!!,
                    email = account.email!!,
                    imageUrl = account.photoUrl.toString()!!,
                    id = account.uid,
                ), navController = navController)
            }
        } catch (e: Exception) {
            Log.d("failed","failed")
        }
    }

    fun googleAuthForFirebase(account: GoogleSignInAccount,navController: NavController) {
        auth = FirebaseAuth.getInstance()
        val credentials = GoogleAuthProvider.getCredential(Constants.CLIENT_ID, null)
        viewModelScope.launch {
            try {
                val response = auth.signInWithCredential(credentials)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main) {
                        Log.d("success1","success1")
                        navController.navigate(Screens.DASHBOARD_SCREEN)
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        Log.d("failure1","failure1")

                    }
                }

            } catch(e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("failed1","failed1")

                }
            }
        }
    }


}