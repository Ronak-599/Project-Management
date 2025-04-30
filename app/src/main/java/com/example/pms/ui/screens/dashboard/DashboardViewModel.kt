package com.example.pms.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pms.data.models.Project
import com.example.pms.data.models.ProjectStatus
import com.example.pms.data.models.Task
import com.example.pms.data.models.TaskAssignment
import com.example.pms.data.models.TaskPriority
import com.example.pms.data.models.TaskStatus
import com.example.pms.data.models.TeamMember
import com.example.pms.data.repository.ProjectRepository
import com.example.pms.data.repository.TaskAssignmentRepository
import com.example.pms.data.repository.TaskRepository
import com.example.pms.data.repository.TeamMemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class DashboardState(
    val recentProjects: List<Project> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val projectCount: Int = 0,
    val taskCount: Int = 0,
    val completedTaskCount: Int = 0,
    val inProgressTaskCount: Int = 0,
    val todoTaskCount: Int = 0,
    val completionRate: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val teamMemberRepository: TeamMemberRepository,
    private val taskAssignmentRepository: TaskAssignmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = combine(
        projectRepository.getAllProjects(),
        taskRepository.getAllTasks()
    ) { projects, tasks ->
        // Sort projects by creation date (newest first) and take the 3 most recent
        val recentProjects = projects.sortedByDescending { it.createdAt }.take(3)
        
        // Get upcoming tasks (not completed, sorted by due date)
        val upcomingTasks = tasks
            .filter { it.status != TaskStatus.COMPLETED && it.dueDate != null }
            .sortedBy { it.dueDate }
            .take(5)
        
        // Calculate statistics
        val projectCount = projects.size
        val taskCount = tasks.size
        val completedTaskCount = tasks.count { it.status == TaskStatus.COMPLETED }
        val inProgressTaskCount = tasks.count { it.status == TaskStatus.IN_PROGRESS }
        val todoTaskCount = tasks.count { it.status == TaskStatus.TODO }
        val completionRate = if (taskCount > 0) (completedTaskCount * 100) / taskCount else 0
        
        DashboardState(
            recentProjects = recentProjects,
            upcomingTasks = upcomingTasks,
            projectCount = projectCount,
            taskCount = taskCount,
            completedTaskCount = completedTaskCount,
            inProgressTaskCount = inProgressTaskCount,
            todoTaskCount = todoTaskCount,
            completionRate = completionRate,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )
    
    // Add some sample data for testing
    init {
        addSampleData()
    }
    
    private fun addSampleData() {
        viewModelScope.launch {
            // Check if we already have data
            val existingProjects = projectRepository.getAllProjects().first()
            if (existingProjects.isNotEmpty()) {
                return@launch // Skip adding sample data if we already have projects
            }
            
            // Add sample projects
            val project1 = Project(
                id = "1",
                name = "Mobile App Development",
                description = "Develop a mobile app for project management",
                startDate = Date(),
                endDate = Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000), // 30 days from now
                status = ProjectStatus.IN_PROGRESS
            )
            
            val project2 = Project(
                id = "2",
                name = "Website Redesign",
                description = "Redesign company website with modern UI",
                startDate = Date(),
                endDate = Date(System.currentTimeMillis() + 15 * 24 * 60 * 60 * 1000), // 15 days from now
                status = ProjectStatus.NOT_STARTED
            )
            
            val project3 = Project(
                id = "3",
                name = "Marketing Campaign",
                description = "Q2 Marketing Campaign for new product launch",
                startDate = Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000), // 10 days ago
                endDate = Date(System.currentTimeMillis() + 20 * 24 * 60 * 60 * 1000), // 20 days from now
                status = ProjectStatus.IN_PROGRESS
            )
            
            projectRepository.insertProject(project1)
            projectRepository.insertProject(project2)
            projectRepository.insertProject(project3)
        }
    }
}
