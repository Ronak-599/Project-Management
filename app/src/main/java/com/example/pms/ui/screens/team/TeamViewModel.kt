package com.example.pms.ui.screens.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pms.data.models.TeamMember
import com.example.pms.data.repository.TeamMemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TeamState(
    val teamMembers: List<TeamMember> = emptyList(),
    val filteredMembers: List<TeamMember> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val showAddDialog: Boolean = false,
    val newMember: TeamMember = TeamMember(
        name = "",
        email = "",
        phone = "",
        role = "",
        skills = emptyList()
    ),
    val editingSkills: String = "",
    val showSkillsEditor: Boolean = false
)

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TeamState())
    
    val state: StateFlow<TeamState> = combine(
        _state,
        teamMemberRepository.getAllTeamMembers()
    ) { state, members ->
        val filteredMembers = members.filter { member ->
            state.searchQuery.isEmpty() || 
                member.name.contains(state.searchQuery, ignoreCase = true) ||
                member.email.contains(state.searchQuery, ignoreCase = true) ||
                member.role.contains(state.searchQuery, ignoreCase = true) ||
                member.phone.contains(state.searchQuery, ignoreCase = true) ||
                member.skills.any { it.contains(state.searchQuery, ignoreCase = true) }
        }.sortedBy { it.name }
        
        state.copy(
            teamMembers = members,
            filteredMembers = filteredMembers,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TeamState()
    )
    
    // Search Functions
    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
    
    // Add Member Dialog Functions
    fun onAddMemberClicked() {
        _state.update { 
            it.copy(
                showAddDialog = true,
                newMember = TeamMember(
                    name = "",
                    email = "",
                    phone = "",
                    role = "",
                    skills = emptyList()
                ),
                editingSkills = "",
                showSkillsEditor = false
            ) 
        }
    }
    
    fun onDismissAddDialog() {
        _state.update { it.copy(showAddDialog = false) }
    }
    
    fun onNewMemberNameChanged(name: String) {
        _state.update { it.copy(newMember = it.newMember.copy(name = name)) }
    }
    
    fun onNewMemberEmailChanged(email: String) {
        _state.update { it.copy(newMember = it.newMember.copy(email = email)) }
    }
    
    fun onNewMemberPhoneChanged(phone: String) {
        _state.update { it.copy(newMember = it.newMember.copy(phone = phone)) }
    }
    
    fun onNewMemberRoleChanged(role: String) {
        _state.update { it.copy(newMember = it.newMember.copy(role = role)) }
    }
    
    fun onEditingSkillsChanged(skills: String) {
        _state.update { it.copy(editingSkills = skills) }
    }
    
    fun onAddSkill() {
        val skill = _state.value.editingSkills.trim()
        if (skill.isNotEmpty()) {
            val currentSkills = _state.value.newMember.skills
            if (!currentSkills.contains(skill)) {
                _state.update { 
                    it.copy(
                        newMember = it.newMember.copy(
                            skills = currentSkills + skill
                        ),
                        editingSkills = ""
                    )
                }
            }
        }
    }
    
    fun onRemoveSkill(skill: String) {
        _state.update { 
            it.copy(
                newMember = it.newMember.copy(
                    skills = it.newMember.skills.filter { s -> s != skill }
                )
            )
        }
    }
    
    fun onToggleSkillsEditor() {
        _state.update { it.copy(showSkillsEditor = !it.showSkillsEditor) }
    }
    
    fun onCreateMember() {
        val newMember = state.value.newMember
        if (newMember.name.isBlank() || newMember.email.isBlank()) return
        
        viewModelScope.launch {
            teamMemberRepository.insertTeamMember(newMember)
            _state.update { it.copy(showAddDialog = false) }
        }
    }
    
    // Team Member Management Functions
    fun deleteMember(memberId: String) {
        viewModelScope.launch {
            teamMemberRepository.deleteTeamMember(memberId)
        }
    }
}
