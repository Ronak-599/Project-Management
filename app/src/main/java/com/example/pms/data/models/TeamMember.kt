package com.example.pms.data.models

import java.util.Date
import java.util.UUID

data class TeamMember(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val phone: String = "",
    val role: String,
    val skills: List<String> = emptyList(),
    val createdAt: Date = Date()
)
