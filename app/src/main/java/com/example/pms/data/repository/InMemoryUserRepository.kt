package com.example.pms.data.repository

import com.example.pms.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryUserRepository @Inject constructor() : UserRepository {
    
    private val users = mutableListOf<User>()
    private val usersFlow = MutableStateFlow<List<User>>(users)
    
    override fun getAllUsers(): Flow<List<User>> {
        return usersFlow
    }
    
    override suspend fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }
    
    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email.equals(email, ignoreCase = true) }
    }
    
    override suspend fun insertUser(user: User) {
        users.add(user)
        usersFlow.value = users.toList()
    }
    
    override suspend fun updateUser(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
            usersFlow.value = users.toList()
        }
    }
    
    override suspend fun deleteUser(id: String) {
        val index = users.indexOfFirst { it.id == id }
        if (index != -1) {
            users.removeAt(index)
            usersFlow.value = users.toList()
        }
    }
}
