package com.example.userdirectory.data.repo

import com.example.userdirectory.data.local.UserDao
import com.example.userdirectory.data.remote.RetrofitInstance
import com.example.userdirectory.data.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import com.example.userdirectory.data.local.UserEntity

class UserRepository(private val userDao: UserDao) {

    // UI always observes Room flows
    fun usersFlow(): Flow<List<UserEntity>> = userDao.getAllUsers()

    fun searchFlow(query: String): Flow<List<UserEntity>> =
        if (query.isBlank()) userDao.getAllUsers() else userDao.searchUsers(query)

    suspend fun refreshUsers() = withContext(Dispatchers.IO) {
        try {
            val remote = RetrofitInstance.api.getUsers()
            userDao.insertAll(remote.map { it.toEntity() })
        } catch (_: Exception) {
            // offline first: ignore errors, keep using cached Room data
        }
    }
}