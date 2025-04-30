package com.example.pms.data.auth

import com.example.pms.data.models.User
import com.example.pms.data.repository.UserRepository
import com.example.pms.utils.PasswordUtils
import com.example.pms.utils.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser
    
    init {
        // Check if user is already logged in from preferences
        if (preferencesManager.isLoggedIn()) {
            val userId = preferencesManager.getLoggedInUserId()
            if (userId != null) {
                // Load user data in the next frame to avoid blocking the UI
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val user = userRepository.getUserById(userId)
                        if (user != null) {
                            _currentUser.value = user
                        } else {
                            // User not found in database, clear preferences
                            preferencesManager.clearUserLoginInfo()
                        }
                    } catch (e: Exception) {
                        // Error loading user, clear preferences
                        preferencesManager.clearUserLoginInfo()
                    }
                }
            }
        }
    }
    
    suspend fun register(name: String, email: String, password: String): AuthResult {
        try {
            // Check if user already exists
            val existingUser = userRepository.getUserByEmail(email)
            if (existingUser != null) {
                return AuthResult.Error("User with this email already exists")
            }
            
            // Create new user with hashed password
            val hashedPassword = PasswordUtils.hashPassword(password)
            val newUser = User(
                name = name,
                email = email,
                password = hashedPassword
            )
            
            // Insert user into database
            userRepository.insertUser(newUser)
            
            // Verify user was inserted by retrieving it again
            val insertedUser = userRepository.getUserByEmail(email)
            if (insertedUser == null) {
                return AuthResult.Error("Failed to save user to database")
            }
            
            // Set current user
            _currentUser.value = insertedUser
            
            // Save login state to preferences
            preferencesManager.saveUserLoginInfo(insertedUser.id, insertedUser.email)
            
            return AuthResult.Success(insertedUser)
        } catch (e: Exception) {
            e.printStackTrace() // Print stack trace for debugging
            return AuthResult.Error("Registration failed: ${e.localizedMessage ?: "Unknown error"}")
        }
    }
    
    suspend fun login(email: String, password: String): AuthResult {
        try {
            // Get user by email
            val user = userRepository.getUserByEmail(email)
            
            if (user == null) {
                return AuthResult.Error("User not found with email: $email")
            }
            
            // Check password
            val passwordMatches = PasswordUtils.verifyPassword(password, user.password)
            
            return if (passwordMatches) {
                // Set current user
                _currentUser.value = user
                
                // Save login state to preferences
                preferencesManager.saveUserLoginInfo(user.id, user.email)
                
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Invalid password for user: $email")
            }
        } catch (e: Exception) {
            e.printStackTrace() // Print stack trace for debugging
            return AuthResult.Error("Login failed: ${e.localizedMessage ?: "Unknown error"}")
        }
    }
    
    fun logout() {
        _currentUser.value = null
        
        // Clear login state from preferences
        preferencesManager.clearUserLoginInfo()
    }
    
    fun isLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
