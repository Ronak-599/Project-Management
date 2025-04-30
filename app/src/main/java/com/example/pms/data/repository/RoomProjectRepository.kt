package com.example.pms.data.repository

import com.example.pms.data.database.dao.ProjectDao
import com.example.pms.data.database.entities.ProjectEntity
import com.example.pms.data.models.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) : ProjectRepository {
    
    override fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAllProjects().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override suspend fun getProjectById(projectId: String): Project? {
        return projectDao.getProjectById(projectId)?.toModel()
    }
    
    override suspend fun insertProject(project: Project) {
        projectDao.insertProject(ProjectEntity.fromModel(project))
    }
    
    override suspend fun updateProject(project: Project) {
        projectDao.updateProject(ProjectEntity.fromModel(project))
    }
    
    override suspend fun deleteProject(projectId: String) {
        projectDao.deleteProjectById(projectId)
    }
}
