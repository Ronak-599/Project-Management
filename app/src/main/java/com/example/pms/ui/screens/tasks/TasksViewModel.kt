package com.example.pms.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pms.data.models.Project
import com.example.pms.data.models.Task
import com.example.pms.data.models.TaskPriority
import com.example.pms.data.models.TaskStatus
import com.example.pms.data.repository.ProjectRepository
import com.example.pms.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class TasksState(
    val tasks: List<Task> = emptyList(),
    val projects: List<Project> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val statusFilter: TaskStatus? = null,
    val priorityFilter: TaskPriority? = null,
    val projectFilter: String? = null,
    val showAddDialog: Boolean = false,
    val newTask: Task = Task(
        projectId = "",
        title = "",
        description = "",
        status = TaskStatus.TODO,
        priority = TaskPriority.MEDIUM,
        dueDate = null
    )
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TasksState())
    
    val state: StateFlow<TasksState> = combine(
        _state,
        taskRepository.getAllTasks(),
        projectRepository.getAllProjects()
    ) { state, tasks, projects ->
        val filteredTasks = tasks.filter { task ->
            val matchesSearch = state.searchQuery.isEmpty() || 
                task.title.contains(state.searchQuery, ignoreCase = true) ||
                task.description.contains(state.searchQuery, ignoreCase = true)
                
            val matchesStatus = state.statusFilter == null || task.status == state.statusFilter
            
            val matchesPriority = state.priorityFilter == null || task.priority == state.priorityFilter
            
            val matchesProject = state.projectFilter == null || task.projectId == state.projectFilter
            
            matchesSearch && matchesStatus && matchesPriority && matchesProject
        }.sortedByDescending { it.createdAt }
        
        state.copy(
            tasks = tasks,
            projects = projects,
            filteredTasks = filteredTasks,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksState()
    )
    
    // Search and Filter Functions
    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
    
    fun onStatusFilterChanged(status: TaskStatus?) {
        _state.update { it.copy(statusFilter = status) }
    }
    
    fun onPriorityFilterChanged(priority: TaskPriority?) {
        _state.update { it.copy(priorityFilter = priority) }
    }
    
    fun onProjectFilterChanged(projectId: String?) {
        _state.update { it.copy(projectFilter = projectId) }
    }
    
    fun clearFilters() {
        _state.update { 
            it.copy(
                searchQuery = "",
                statusFilter = null,
                priorityFilter = null,
                projectFilter = null
            ) 
        }
    }
    
    // Add Task Dialog Functions
    fun onAddTaskClicked() {
        _state.update { 
            it.copy(
                showAddDialog = true,
                newTask = Task(
                    projectId = it.projects.firstOrNull()?.id ?: "",
                    title = "",
                    description = "",
                    status = TaskStatus.TODO,
                    priority = TaskPriority.MEDIUM,
                    dueDate = null
                )
            ) 
        }
    }
    
    fun onDismissAddDialog() {
        _state.update { it.copy(showAddDialog = false) }
    }
    
    fun onNewTaskTitleChanged(title: String) {
        _state.update { it.copy(newTask = it.newTask.copy(title = title)) }
    }
    
    fun onNewTaskDescriptionChanged(description: String) {
        _state.update { it.copy(newTask = it.newTask.copy(description = description)) }
    }
    
    fun onNewTaskProjectChanged(projectId: String) {
        _state.update { it.copy(newTask = it.newTask.copy(projectId = projectId)) }
    }
    
    fun onNewTaskStatusChanged(status: TaskStatus) {
        _state.update { it.copy(newTask = it.newTask.copy(status = status)) }
    }
    
    fun onNewTaskPriorityChanged(priority: TaskPriority) {
        _state.update { it.copy(newTask = it.newTask.copy(priority = priority)) }
    }
    
    fun onNewTaskDueDateChanged(dueDate: Date?) {
        _state.update { it.copy(newTask = it.newTask.copy(dueDate = dueDate)) }
    }
    
    fun onCreateTask() {
        val newTask = state.value.newTask
        if (newTask.title.isBlank() || newTask.projectId.isBlank()) return
        
        viewModelScope.launch {
            taskRepository.insertTask(newTask)
            _state.update { it.copy(showAddDialog = false) }
        }
    }
    
    // Task Management Functions
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }
    
    fun updateTaskStatus(task: Task, newStatus: TaskStatus) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(status = newStatus))
        }
    }
}
