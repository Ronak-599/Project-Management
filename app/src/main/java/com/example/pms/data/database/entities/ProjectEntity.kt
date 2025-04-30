package com.example.pms.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pms.data.models.Project
import com.example.pms.data.models.ProjectStatus
import java.util.Date
import java.util.UUID

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val startDate: Date?,
    val endDate: Date?,
    val status: ProjectStatus,
    val createdAt: Date = Date()
) {
    fun toModel(): Project {
        return Project(
            id = id,
            name = name,
            description = description,
            startDate = startDate,
            endDate = endDate,
            status = status,
            createdAt = createdAt
        )
    }
    
    companion object {
        fun fromModel(project: Project): ProjectEntity {
            return ProjectEntity(
                id = project.id,
                name = project.name,
                description = project.description,
                startDate = project.startDate,
                endDate = project.endDate,
                status = project.status,
                createdAt = project.createdAt
            )
        }
    }
}
