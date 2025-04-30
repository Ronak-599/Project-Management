package com.example.pms.data.repository

import com.example.pms.data.database.dao.TaskAssignmentDao
import com.example.pms.data.database.entities.TaskAssignmentEntity
import com.example.pms.data.models.TaskAssignment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomTaskAssignmentRepository @Inject constructor(
    private val taskAssignmentDao: TaskAssignmentDao
) : TaskAssignmentRepository {
    
    override fun getAllTaskAssignments(): Flow<List<TaskAssignment>> {
        return taskAssignmentDao.getAllTaskAssignments().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override fun getAssignmentsByTaskId(taskId: String): Flow<List<TaskAssignment>> {
        return taskAssignmentDao.getAssignmentsByTaskId(taskId).map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override fun getAssignmentsByMemberId(memberId: String): Flow<List<TaskAssignment>> {
        return taskAssignmentDao.getAssignmentsByMemberId(memberId).map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override suspend fun getTaskAssignmentById(id: String): TaskAssignment? {
        return taskAssignmentDao.getTaskAssignmentById(id)?.toModel()
    }
    
    override suspend fun insertTaskAssignment(taskAssignment: TaskAssignment) {
        taskAssignmentDao.insertTaskAssignment(TaskAssignmentEntity.fromModel(taskAssignment))
    }
    
    override suspend fun deleteTaskAssignment(id: String) {
        taskAssignmentDao.deleteTaskAssignmentById(id)
    }
    
    // These methods are not in the interface but might be needed by the app
    // If needed, update the interface to include them
    suspend fun updateTaskAssignment(taskAssignment: TaskAssignment) {
        taskAssignmentDao.updateTaskAssignment(TaskAssignmentEntity.fromModel(taskAssignment))
    }
    
    suspend fun deleteAssignmentsByTaskId(taskId: String) {
        taskAssignmentDao.deleteAssignmentsByTaskId(taskId)
    }
    
    suspend fun deleteAssignmentsByMemberId(memberId: String) {
        taskAssignmentDao.deleteAssignmentsByMemberId(memberId)
    }
}
