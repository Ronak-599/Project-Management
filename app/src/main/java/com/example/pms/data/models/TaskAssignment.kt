package com.example.pms.data.models

import java.util.Date
import java.util.UUID

data class TaskAssignment(
    val id: String = UUID.randomUUID().toString(),
    val taskId: String,
    val memberId: String,
    val assignedAt: Date = Date()
)
