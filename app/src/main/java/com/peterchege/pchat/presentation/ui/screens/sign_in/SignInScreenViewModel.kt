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
package com.peterchege.pchat.presentation.ui.screens.sign_in

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.peterchege.pchat.core.api.requests.AddUser
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.data.OfflineFirstUserRepository
import com.peterchege.pchat.util.Constants
import com.peterchege.pchat.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

import javax.inject.Inject


@HiltViewModel
class SignInScreenViewModel @Inject constructor(

    private val offlineFirstUserRepository: OfflineFirstUserRepository,

    ):ViewModel() {
    private var _user = mutableStateOf<User?>(null)
    var user: State<User?> = _user

    private var _text = mutableStateOf("")
    var text:State<String> = _text

    fun onChangeUser(user: User, navController: NavController){

        addUserToDatabase(user = user,navController = navController)

    }

    private  fun addUserToDatabase(user: User, navController:NavController){
        Log.e("TEST","TEST")
        viewModelScope.launch {
            try{
                val addUser = AddUser(
                    displayName = user.displayName ?: "",
                    email = user.email ?: "",
                    imageUrl = user.imageUrl ,
                    userId = user.userId
                )
                val response = offlineFirstUserRepository.addUser(addUser = addUser)
                response.user?.let {
                    offlineFirstUserRepository.setAuthUser(user = it)

                }
                val authUser = offlineFirstUserRepository.getAuthUser()
                authUser.collectLatest {
                    if (it == null) return@collectLatest
                    navController.navigate(Screens.DASHBOARD_SCREEN)
                }

            }catch (e: HttpException){
                Log.e("HTTP ERROR",e.localizedMessage ?: "Http error")

            }catch (e:IOException){
                Log.e("IO ERROR",e.localizedMessage ?: "IO error")

            }
        }


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