package com.example.pms.data.models

import java.util.Date
import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val startDate: Date?,
    val endDate: Date?,
    val status: ProjectStatus,
    val createdAt: Date = Date()
)

enum class ProjectStatus {
    NOT_STARTED,
    IN_PROGRESS,
    ON_HOLD,
    COMPLETED,
    CANCELLED
}
