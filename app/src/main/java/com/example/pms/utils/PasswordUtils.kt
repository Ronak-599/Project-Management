package com.example.pms.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * Utility class for password hashing and verification
 * This is a simple implementation using SHA-256 with salt
 * For production, consider using a more robust library like BCrypt
 */
object PasswordUtils {
    
    private const val SALT_LENGTH = 16
    
    /**
     * Hashes a password with a random salt using SHA-256
     * @param password The plain text password to hash
     * @return A string containing the salt and hashed password, separated by a colon
     */
    fun hashPassword(password: String): String {
        val salt = generateSalt()
        val hashedPassword = hashWithSalt(password, salt)
        return "${Base64.getEncoder().encodeToString(salt)}:$hashedPassword"
    }
    
    /**
     * Verifies a password against a stored hash
     * @param password The plain text password to verify
     * @param storedHash The stored hash from the database (salt:hash format)
     * @return True if the password matches, false otherwise
     */
    fun verifyPassword(password: String, storedHash: String): Boolean {
        try {
            val parts = storedHash.split(":")
            if (parts.size != 2) {
                println("PasswordUtils: Invalid hash format, expected salt:hash but got: $storedHash")
                return false
            }
            
            val salt = try {
                Base64.getDecoder().decode(parts[0])
            } catch (e: Exception) {
                println("PasswordUtils: Failed to decode salt: ${e.message}")
                return false
            }
            
            val hashedPassword = hashWithSalt(password, salt)
            val matches = hashedPassword == parts[1]
            
            if (!matches) {
                println("PasswordUtils: Password verification failed")
                println("PasswordUtils: Expected: ${parts[1]}")
                println("PasswordUtils: Got: $hashedPassword")
            }
            
            return matches
        } catch (e: Exception) {
            // If any exception occurs during verification, return false
            println("PasswordUtils: Exception during verification: ${e.message}")
            e.printStackTrace()
            return false
        }
    }
    
    /**
     * Generates a random salt
     * @return A byte array containing the salt
     */
    private fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return salt
    }
    
    /**
     * Hashes a password with a given salt using SHA-256
     * @param password The plain text password to hash
     * @param salt The salt to use
     * @return The hashed password as a Base64 encoded string
     */
    private fun hashWithSalt(password: String, salt: ByteArray): String {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(salt)
            val hashedBytes = md.digest(password.toByteArray())
            return Base64.getEncoder().encodeToString(hashedBytes)
        } catch (e: Exception) {
            // If any exception occurs during hashing, throw a more descriptive exception
            throw RuntimeException("Failed to hash password: ${e.message}")
        }
    }
}
