package com.example.pms.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.pms.data.models.TaskAssignment
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "task_assignments",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TeamMemberEntity::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("taskId"), Index("memberId")]
)
data class TaskAssignmentEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val taskId: String,
    val memberId: String,
    val assignedAt: Date = Date()
) {
    fun toModel(): TaskAssignment {
        return TaskAssignment(
            id = id,
            taskId = taskId,
            memberId = memberId,
            assignedAt = assignedAt
        )
    }
    
    companion object {
        fun fromModel(taskAssignment: TaskAssignment): TaskAssignmentEntity {
            return TaskAssignmentEntity(
                id = taskAssignment.id,
                taskId = taskAssignment.taskId,
                memberId = taskAssignment.memberId,
                assignedAt = taskAssignment.assignedAt
            )
        }
    }
}
