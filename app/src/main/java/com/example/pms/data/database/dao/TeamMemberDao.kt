package com.example.pms.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pms.data.database.entities.TeamMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamMemberDao {
    @Query("SELECT * FROM team_members")
    fun getAllTeamMembers(): Flow<List<TeamMemberEntity>>
    
    @Query("SELECT * FROM team_members WHERE id = :memberId")
    suspend fun getTeamMemberById(memberId: String): TeamMemberEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamMember(teamMember: TeamMemberEntity)
    
    @Update
    suspend fun updateTeamMember(teamMember: TeamMemberEntity)
    
    @Delete
    suspend fun deleteTeamMember(teamMember: TeamMemberEntity)
    
    @Query("DELETE FROM team_members WHERE id = :memberId")
    suspend fun deleteTeamMemberById(memberId: String)
}
