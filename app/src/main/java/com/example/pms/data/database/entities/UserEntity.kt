package com.example.pms.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pms.data.models.User
import java.util.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: Long
) {
    fun toUser(): User {
        return User(
            id = id,
            name = name,
            email = email,
            password = password,
            createdAt = Date(createdAt)
        )
    }
    
    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                name = user.name,
                email = user.email,
                password = user.password,
                createdAt = user.createdAt.time
            )
        }
    }
}
