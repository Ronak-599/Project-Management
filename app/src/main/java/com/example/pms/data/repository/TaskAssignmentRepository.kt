package com.example.pms.data.repository

import com.example.pms.data.models.TaskAssignment
import kotlinx.coroutines.flow.Flow

interface TaskAssignmentRepository {
    fun getAllTaskAssignments(): Flow<List<TaskAssignment>>
    fun getAssignmentsByTaskId(taskId: String): Flow<List<TaskAssignment>>
    fun getAssignmentsByMemberId(memberId: String): Flow<List<TaskAssignment>>
    suspend fun getTaskAssignmentById(id: String): TaskAssignment?
    suspend fun insertTaskAssignment(taskAssignment: TaskAssignment)
    suspend fun deleteTaskAssignment(id: String)
}
