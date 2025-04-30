package com.example.pms.data.repository

import com.example.pms.data.models.TeamMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryTeamMemberRepository : TeamMemberRepository {
    private val teamMembers = MutableStateFlow<List<TeamMember>>(emptyList())

    override fun getAllTeamMembers(): Flow<List<TeamMember>> = teamMembers.asStateFlow()

    override suspend fun getTeamMemberById(id: String): TeamMember? {
        return teamMembers.value.find { it.id == id }
    }

    override suspend fun insertTeamMember(teamMember: TeamMember) {
        teamMembers.update { currentMembers ->
            currentMembers + teamMember
        }
    }

    override suspend fun updateTeamMember(teamMember: TeamMember) {
        teamMembers.update { currentMembers ->
            currentMembers.map {
                if (it.id == teamMember.id) teamMember else it
            }
        }
    }

    override suspend fun deleteTeamMember(id: String) {
        teamMembers.update { currentMembers ->
            currentMembers.filter { it.id != id }
        }
    }
}
