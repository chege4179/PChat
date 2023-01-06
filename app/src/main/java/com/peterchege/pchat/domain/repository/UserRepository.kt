package com.peterchege.pchat.domain.repository

import com.peterchege.pchat.data.api.requests.AddUser
import com.peterchege.pchat.data.api.responses.AddUserResponse
import com.peterchege.pchat.data.api.responses.GetUserByIdResponse
import com.peterchege.pchat.data.api.responses.SearchUserResponse

interface UserRepository {

    suspend fun addUser(addUser: AddUser): AddUserResponse

    suspend fun searchUser(query:String): SearchUserResponse

    suspend fun getUserById(id:String): GetUserByIdResponse
}