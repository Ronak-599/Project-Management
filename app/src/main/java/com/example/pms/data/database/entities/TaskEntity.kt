package com.example.pms.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.pms.data.models.Task
import com.example.pms.data.models.TaskPriority
import com.example.pms.data.models.TaskStatus
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class TaskEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val dueDate: Date?,
    val createdAt: Date = Date()
) {
    fun toModel(): Task {
        return Task(
            id = id,
            projectId = projectId,
            title = title,
            description = description,
            status = status,
            priority = priority,
            dueDate = dueDate,
            createdAt = createdAt
        )
    }
    
    companion object {
        fun fromModel(task: Task): TaskEntity {
            return TaskEntity(
                id = task.id,
                projectId = task.projectId,
                title = task.title,
                description = task.description,
                status = task.status,
                priority = task.priority,
                dueDate = task.dueDate,
                createdAt = task.createdAt
            )
        }
    }
}
