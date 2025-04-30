package com.example.pms.data.repository

import com.example.pms.data.models.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryProjectRepository : ProjectRepository {
    private val projects = MutableStateFlow<List<Project>>(emptyList())

    override fun getAllProjects(): Flow<List<Project>> = projects.asStateFlow()

    override suspend fun getProjectById(id: String): Project? {
        return projects.value.find { it.id == id }
    }

    override suspend fun insertProject(project: Project) {
        projects.update { currentProjects ->
            currentProjects + project
        }
    }

    override suspend fun updateProject(project: Project) {
        projects.update { currentProjects ->
            currentProjects.map {
                if (it.id == project.id) project else it
            }
        }
    }

    override suspend fun deleteProject(id: String) {
        projects.update { currentProjects ->
            currentProjects.filter { it.id != id }
        }
    }
}
