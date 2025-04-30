package com.example.pms.data.repository

import com.example.pms.data.database.dao.TaskDao
import com.example.pms.data.database.entities.TaskEntity
import com.example.pms.data.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomTaskRepository @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override fun getTasksByProjectId(projectId: String): Flow<List<Task>> {
        return taskDao.getTasksByProjectId(projectId).map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override suspend fun getTaskById(taskId: String): Task? {
        return taskDao.getTaskById(taskId)?.toModel()
    }
    
    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(TaskEntity.fromModel(task))
    }
    
    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(TaskEntity.fromModel(task))
    }
    
    override suspend fun deleteTask(id: String) {
        taskDao.deleteTaskById(id)
    }
    
    // This method is not in the interface but might be needed by the app
    // If needed, update the interface to include it
    suspend fun deleteTasksByProjectId(projectId: String) {
        taskDao.deleteTasksByProjectId(projectId)
    }
}
