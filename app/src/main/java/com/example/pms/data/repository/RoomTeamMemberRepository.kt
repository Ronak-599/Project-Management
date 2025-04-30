package com.example.pms.data.repository

import com.example.pms.data.database.dao.TeamMemberDao
import com.example.pms.data.database.entities.TeamMemberEntity
import com.example.pms.data.models.TeamMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomTeamMemberRepository @Inject constructor(
    private val teamMemberDao: TeamMemberDao
) : TeamMemberRepository {
    
    override fun getAllTeamMembers(): Flow<List<TeamMember>> {
        return teamMemberDao.getAllTeamMembers().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override suspend fun getTeamMemberById(memberId: String): TeamMember? {
        return teamMemberDao.getTeamMemberById(memberId)?.toModel()
    }
    
    override suspend fun insertTeamMember(teamMember: TeamMember) {
        teamMemberDao.insertTeamMember(TeamMemberEntity.fromModel(teamMember))
    }
    
    override suspend fun updateTeamMember(teamMember: TeamMember) {
        teamMemberDao.updateTeamMember(TeamMemberEntity.fromModel(teamMember))
    }
    
    override suspend fun deleteTeamMember(memberId: String) {
        teamMemberDao.deleteTeamMemberById(memberId)
    }
}
