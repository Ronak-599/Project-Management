package com.example.pms.data.repository

import com.example.pms.data.models.TaskAssignment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class InMemoryTaskAssignmentRepository : TaskAssignmentRepository {
    private val assignments = MutableStateFlow<List<TaskAssignment>>(emptyList())

    override fun getAllTaskAssignments(): Flow<List<TaskAssignment>> = assignments.asStateFlow()

    override fun getAssignmentsByTaskId(taskId: String): Flow<List<TaskAssignment>> {
        return assignments.map { assignmentList ->
            assignmentList.filter { it.taskId == taskId }
        }
    }

    override fun getAssignmentsByMemberId(memberId: String): Flow<List<TaskAssignment>> {
        return assignments.map { assignmentList ->
            assignmentList.filter { it.memberId == memberId }
        }
    }

    override suspend fun getTaskAssignmentById(id: String): TaskAssignment? {
        return assignments.value.find { it.id == id }
    }

    override suspend fun insertTaskAssignment(taskAssignment: TaskAssignment) {
        assignments.update { currentAssignments ->
            currentAssignments + taskAssignment
        }
    }

    override suspend fun deleteTaskAssignment(id: String) {
        assignments.update { currentAssignments ->
            currentAssignments.filter { it.id != id }
        }
    }
}
