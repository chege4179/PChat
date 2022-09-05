package com.peterchege.pchat.ui.screens.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.pchat.api.PChatApi
import com.peterchege.pchat.api.requests.AddUser
import com.peterchege.pchat.repositories.UserRepository
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.SocketHandler.mSocket
import com.peterchege.pchat.util.getGoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
):ViewModel() {
    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> =_isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg: State<String> =_errorMsg

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> =_isLoading

    private val _msg = mutableStateOf("")
    val msg: State<String> = _msg

    val displayName = sharedPreferences.getString(Constants.USER_DISPLAY_NAME,null)
    val imageUrl = sharedPreferences.getString(Constants.USER_IMAGE_URL,null)
    val email = sharedPreferences.getString(Constants.USER_EMAIL,null)

    init{
//        addUserToDatabase()
        mSocket.emit("connected","$email has connected")

    }



    fun logoutUser(navController:NavController,context: Context){
        val signInClient = getGoogleSignInClient(context = context)

        sharedPreferences.edit().remove(Constants.USER_ID).commit()
        sharedPreferences.edit().remove(Constants.USER_DISPLAY_NAME).commit()
        sharedPreferences.edit().remove(Constants.USER_EMAIL).commit()
        sharedPreferences.edit().remove(Constants.USER_IMAGE_URL).commit()
        signInClient.signOut()
        navController.navigate(Screens.SIGN_IN_SCREEN)


    }

}