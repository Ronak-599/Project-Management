package com.example.pms.data.repository

import com.example.pms.data.models.TeamMember
import kotlinx.coroutines.flow.Flow

interface TeamMemberRepository {
    fun getAllTeamMembers(): Flow<List<TeamMember>>
    suspend fun getTeamMemberById(id: String): TeamMember?
    suspend fun insertTeamMember(teamMember: TeamMember)
    suspend fun updateTeamMember(teamMember: TeamMember)
    suspend fun deleteTeamMember(id: String)
}
