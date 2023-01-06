package com.peterchege.pchat.data.repositories

import com.peterchege.pchat.data.api.PChatApi
import com.peterchege.pchat.data.api.requests.AddUser
import com.peterchege.pchat.data.api.responses.AddUserResponse
import com.peterchege.pchat.data.api.responses.GetUserByIdResponse
import com.peterchege.pchat.data.api.responses.SearchUserResponse
import com.peterchege.pchat.domain.repository.UserRepository
import javax.inject.Inject

class OfflineFirstUserRepository @Inject constructor(
    private val api: PChatApi
): UserRepository{
    override suspend fun addUser(addUser: AddUser): AddUserResponse {
        return api.addUser(addUser = addUser)
    }
    override suspend fun searchUser(query:String): SearchUserResponse {
        return api.searchUser(query = query)
    }
    override suspend fun getUserById(id:String): GetUserByIdResponse {
        return api.getUserById(id = id)
    }
}