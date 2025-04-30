package com.example.pms.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pms.data.database.converters.StringListConverter
import com.example.pms.data.models.TeamMember
import java.util.Date
import java.util.UUID

@Entity(tableName = "team_members")
@TypeConverters(StringListConverter::class)
data class TeamMemberEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val phone: String = "",
    val role: String,
    val skills: List<String> = emptyList(),
    val createdAt: Date = Date()
) {
    fun toModel(): TeamMember {
        return TeamMember(
            id = id,
            name = name,
            email = email,
            phone = phone,
            role = role,
            skills = skills,
            createdAt = createdAt
        )
    }
    
    companion object {
        fun fromModel(teamMember: TeamMember): TeamMemberEntity {
            return TeamMemberEntity(
                id = teamMember.id,
                name = teamMember.name,
                email = teamMember.email,
                phone = teamMember.phone,
                role = teamMember.role,
                skills = teamMember.skills,
                createdAt = teamMember.createdAt
            )
        }
    }
}
