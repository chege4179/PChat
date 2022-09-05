package com.peterchege.pchat.ui.navigation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,

) :ViewModel() {
    fun getInitialRoute():String {
        val username = sharedPreferences.getString(Constants.USER_DISPLAY_NAME,null)
        if (username === null){
            return Screens.SIGN_IN_SCREEN
        }else{
            return Screens.DASHBOARD_SCREEN
        }
    }
}