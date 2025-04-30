package com.example.pms.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pms.data.models.Project
import com.example.pms.data.models.ProjectStatus
import com.example.pms.data.repository.ProjectRepository
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

data class ProjectsState(
    val projects: List<Project> = emptyList(),
    val filteredProjects: List<Project> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: ProjectStatus? = null,
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val newProject: Project = Project(
        name = "",
        description = "",
        startDate = Date(),
        endDate = null,
        status = ProjectStatus.NOT_STARTED
    )
)

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectsState())
    
    val state: StateFlow<ProjectsState> = combine(
        _state,
        projectRepository.getAllProjects()
    ) { state, projects ->
        val filteredProjects = projects.filter { project ->
            val matchesSearch = project.name.contains(state.searchQuery, ignoreCase = true) ||
                    project.description.contains(state.searchQuery, ignoreCase = true)
            val matchesStatus = state.statusFilter == null || project.status == state.statusFilter
            
            matchesSearch && matchesStatus
        }
        
        state.copy(
            projects = projects,
            filteredProjects = filteredProjects,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProjectsState()
    )
    
    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
    
    fun onStatusFilterChanged(status: ProjectStatus?) {
        _state.update { it.copy(statusFilter = status) }
    }
    
    fun onAddProjectClicked() {
        _state.update { it.copy(showAddDialog = true) }
    }
    
    fun onDismissAddDialog() {
        _state.update { 
            it.copy(
                showAddDialog = false,
                newProject = Project(
                    name = "",
                    description = "",
                    startDate = Date(),
                    endDate = null,
                    status = ProjectStatus.NOT_STARTED
                )
            ) 
        }
    }
    
    fun onNewProjectNameChanged(name: String) {
        _state.update { it.copy(newProject = it.newProject.copy(name = name)) }
    }
    
    fun onNewProjectDescriptionChanged(description: String) {
        _state.update { it.copy(newProject = it.newProject.copy(description = description)) }
    }
    
    fun onNewProjectStartDateChanged(startDate: Date) {
        _state.update { it.copy(newProject = it.newProject.copy(startDate = startDate)) }
    }
    
    fun onNewProjectEndDateChanged(endDate: Date?) {
        _state.update { it.copy(newProject = it.newProject.copy(endDate = endDate)) }
    }
    
    fun onNewProjectStatusChanged(status: ProjectStatus) {
        _state.update { it.copy(newProject = it.newProject.copy(status = status)) }
    }
    
    fun onCreateProject() {
        val newProject = state.value.newProject
        if (newProject.name.isBlank()) return
        
        viewModelScope.launch {
            projectRepository.insertProject(newProject)
            _state.update { 
                it.copy(
                    showAddDialog = false,
                    newProject = Project(
                        name = "",
                        description = "",
                        startDate = Date(),
                        endDate = null,
                        status = ProjectStatus.NOT_STARTED
                    )
                ) 
            }
        }
    }
    
    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            projectRepository.deleteProject(projectId)
        }
    }
}
