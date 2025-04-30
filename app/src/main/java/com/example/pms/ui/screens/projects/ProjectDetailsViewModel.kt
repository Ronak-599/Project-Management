package com.example.pms.ui.screens.projects

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pms.data.models.Project
import com.example.pms.data.models.ProjectStatus
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

data class ProjectDetailsState(
    val project: Project? = null,
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val showAddTaskDialog: Boolean = false,
    val showEditProjectDialog: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val newTask: Task = Task(
        projectId = "",
        title = "",
        description = "",
        status = TaskStatus.TODO,
        priority = TaskPriority.MEDIUM,
        dueDate = null
    ),
    val editedProject: Project? = null
)

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])
    
    private val _state = MutableStateFlow(ProjectDetailsState())
    
    val state: StateFlow<ProjectDetailsState> = combine(
        _state,
        projectRepository.getAllProjects(),
        taskRepository.getTasksByProjectId(projectId)
    ) { state, projects, tasks ->
        val project = projects.find { it.id == projectId }
        
        state.copy(
            project = project,
            tasks = tasks,
            isLoading = false,
            editedProject = state.editedProject ?: project,
            newTask = state.newTask.copy(projectId = projectId)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProjectDetailsState()
    )
    
    // Task Dialog Functions
    fun onAddTaskClicked() {
        _state.update { 
            it.copy(
                showAddTaskDialog = true,
                newTask = Task(
                    projectId = projectId,
                    title = "",
                    description = "",
                    status = TaskStatus.TODO,
                    priority = TaskPriority.MEDIUM,
                    dueDate = null
                )
            ) 
        }
    }
    
    fun onDismissAddTaskDialog() {
        _state.update { it.copy(showAddTaskDialog = false) }
    }
    
    fun onNewTaskTitleChanged(title: String) {
        _state.update { it.copy(newTask = it.newTask.copy(title = title)) }
    }
    
    fun onNewTaskDescriptionChanged(description: String) {
        _state.update { it.copy(newTask = it.newTask.copy(description = description)) }
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
        if (newTask.title.isBlank()) return
        
        viewModelScope.launch {
            taskRepository.insertTask(newTask)
            _state.update { it.copy(showAddTaskDialog = false) }
        }
    }
    
    // Project Edit Functions
    fun onEditProjectClicked() {
        _state.update { it.copy(showEditProjectDialog = true) }
    }
    
    fun onDismissEditProjectDialog() {
        _state.update { it.copy(showEditProjectDialog = false) }
    }
    
    fun onProjectNameChanged(name: String) {
        _state.update { it.copy(editedProject = it.editedProject?.copy(name = name)) }
    }
    
    fun onProjectDescriptionChanged(description: String) {
        _state.update { it.copy(editedProject = it.editedProject?.copy(description = description)) }
    }
    
    fun onProjectStartDateChanged(startDate: Date) {
        _state.update { it.copy(editedProject = it.editedProject?.copy(startDate = startDate)) }
    }
    
    fun onProjectEndDateChanged(endDate: Date?) {
        _state.update { it.copy(editedProject = it.editedProject?.copy(endDate = endDate)) }
    }
    
    fun onProjectStatusChanged(status: ProjectStatus) {
        _state.update { it.copy(editedProject = it.editedProject?.copy(status = status)) }
    }
    
    fun onUpdateProject() {
        val editedProject = state.value.editedProject ?: return
        if (editedProject.name.isBlank()) return
        
        viewModelScope.launch {
            projectRepository.updateProject(editedProject)
            _state.update { it.copy(showEditProjectDialog = false) }
        }
    }
    
    // Delete Project Functions
    fun onDeleteProjectClicked() {
        _state.update { it.copy(showDeleteConfirmation = true) }
    }
    
    fun onDismissDeleteConfirmation() {
        _state.update { it.copy(showDeleteConfirmation = false) }
    }
    
    fun onConfirmDeleteProject() {
        viewModelScope.launch {
            projectRepository.deleteProject(projectId)
            // Navigation will be handled by the caller
            _state.update { it.copy(showDeleteConfirmation = false) }
        }
    }
    
    // Task Functions
    fun onDeleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }
    
    fun onUpdateTaskStatus(task: Task, newStatus: TaskStatus) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(status = newStatus))
        }
    }
}
