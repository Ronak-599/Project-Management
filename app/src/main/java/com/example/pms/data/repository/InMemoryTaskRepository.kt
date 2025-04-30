package com.example.pms.data.repository

import com.example.pms.data.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class InMemoryTaskRepository : TaskRepository {
    private val tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun getAllTasks(): Flow<List<Task>> = tasks.asStateFlow()

    override fun getTasksByProjectId(projectId: String): Flow<List<Task>> {
        return tasks.map { taskList ->
            taskList.filter { it.projectId == projectId }
        }
    }

    override suspend fun getTaskById(id: String): Task? {
        return tasks.value.find { it.id == id }
    }

    override suspend fun insertTask(task: Task) {
        tasks.update { currentTasks ->
            currentTasks + task
        }
    }

    override suspend fun updateTask(task: Task) {
        tasks.update { currentTasks ->
            currentTasks.map {
                if (it.id == task.id) task else it
            }
        }
    }

    override suspend fun deleteTask(id: String) {
        tasks.update { currentTasks ->
            currentTasks.filter { it.id != id }
        }
    }
}
