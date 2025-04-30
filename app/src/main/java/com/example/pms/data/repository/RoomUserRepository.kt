package com.example.pms.data.repository

import com.example.pms.data.database.dao.UserDao
import com.example.pms.data.database.entities.UserEntity
import com.example.pms.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUserRepository @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    
    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toUser() }
        }
    }
    
    override suspend fun getUserById(id: String): User? {
        return userDao.getUserById(id)?.toUser()
    }
    
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toUser()
    }
    
    override suspend fun insertUser(user: User) {
        userDao.insertUser(UserEntity.fromUser(user))
    }
    
    override suspend fun updateUser(user: User) {
        userDao.updateUser(UserEntity.fromUser(user))
    }
    
    override suspend fun deleteUser(id: String) {
        userDao.deleteUser(id)
    }
}
