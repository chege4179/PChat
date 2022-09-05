package com.peterchege.pchat.repositories

import com.peterchege.pchat.api.PChatApi
import com.peterchege.pchat.api.requests.AddUser
import com.peterchege.pchat.api.responses.AddUserResponse
import com.peterchege.pchat.api.responses.GetUserByIdResponse
import com.peterchege.pchat.api.responses.SearchUserResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: PChatApi
){
    suspend fun addUser(addUser: AddUser):AddUserResponse{
        return api.addUser(addUser = addUser)
    }
    suspend fun searchUser(query:String):SearchUserResponse{
        return api.searchUser(query = query)
    }
    suspend fun getUserById(id:String):GetUserByIdResponse{
        return api.getUserById(id = id)
    }
}