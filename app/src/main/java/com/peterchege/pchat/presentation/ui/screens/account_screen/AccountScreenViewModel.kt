package com.peterchege.pchat.presentation.ui.screens.account_screen

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.pchat.data.repositories.OfflineFirstChatRepository
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import com.peterchege.pchat.util.getGoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountScreenViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val offlineFirstChatRepository: OfflineFirstChatRepository

) :ViewModel(){

    val displayName = sharedPreferences.getString(Constants.USER_DISPLAY_NAME,null)
    val imageUrl = sharedPreferences.getString(Constants.USER_IMAGE_URL,null)
    val email = sharedPreferences.getString(Constants.USER_EMAIL,null)


    fun logoutUser(navController: NavController, context: Context){
        val signInClient = getGoogleSignInClient(context = context)

        sharedPreferences.edit().remove(Constants.USER_ID).commit()
        sharedPreferences.edit().remove(Constants.USER_DISPLAY_NAME).commit()
        sharedPreferences.edit().remove(Constants.USER_EMAIL).commit()
        sharedPreferences.edit().remove(Constants.USER_IMAGE_URL).commit()
        signInClient.signOut()
        viewModelScope.launch {
            offlineFirstChatRepository.clearChats()
            offlineFirstChatRepository.clearMessages()
        }
        navController.navigate(Screens.SIGN_IN_SCREEN)


    }


}