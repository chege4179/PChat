package com.peterchege.pchat.data.repositories

import com.peterchege.pchat.data.api.PChatApi
import com.peterchege.pchat.data.api.requests.AddUser
import com.peterchege.pchat.data.api.responses.AddUserResponse
import com.peterchege.pchat.data.api.responses.GetUserByIdResponse
import com.peterchege.pchat.data.api.responses.SearchUserResponse
import com.peterchege.pchat.data.room.database.PChatDatabase
import com.peterchege.pchat.domain.models.User
import com.peterchege.pchat.domain.repository.UserRepository
import javax.inject.Inject

class OfflineFirstUserRepository @Inject constructor(
    private val api: PChatApi,
    private val db:PChatDatabase
): UserRepository{
    override suspend fun addUser(addUser: AddUser): AddUserResponse {
        return api.addUser(addUser = addUser)
    }
    override suspend fun searchUser(query:String): SearchUserResponse {
        return api.searchUser(query = query)
    }
    override suspend fun getUserById(id:String): GetUserByIdResponse {
        val localChat = db.chatDao.getChatById(receiverGoogleId = id)
        return if (localChat == null){
            api.getUserById(id = id)
        }else{
            GetUserByIdResponse(
                msg = "User fetched successfully",
                success = true,
                user = User(
                    displayName = localChat.name,
                    email = localChat.email,
                    imageUrl = localChat.imageUrl,
                    userId = localChat.googleId
                )

            )
        }

    }
}