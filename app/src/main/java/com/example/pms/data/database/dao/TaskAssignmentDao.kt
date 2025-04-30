package com.example.pms.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pms.data.database.entities.TaskAssignmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskAssignmentDao {
    @Query("SELECT * FROM task_assignments")
    fun getAllTaskAssignments(): Flow<List<TaskAssignmentEntity>>
    
    @Query("SELECT * FROM task_assignments WHERE taskId = :taskId")
    fun getAssignmentsByTaskId(taskId: String): Flow<List<TaskAssignmentEntity>>
    
    @Query("SELECT * FROM task_assignments WHERE memberId = :memberId")
    fun getAssignmentsByMemberId(memberId: String): Flow<List<TaskAssignmentEntity>>
    
    @Query("SELECT * FROM task_assignments WHERE id = :id LIMIT 1")
    suspend fun getTaskAssignmentById(id: String): TaskAssignmentEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAssignment(taskAssignment: TaskAssignmentEntity)
    
    @Update
    suspend fun updateTaskAssignment(taskAssignment: TaskAssignmentEntity)
    
    @Delete
    suspend fun deleteTaskAssignment(taskAssignment: TaskAssignmentEntity)
    
    @Query("DELETE FROM task_assignments WHERE id = :assignmentId")
    suspend fun deleteTaskAssignmentById(assignmentId: String)
    
    @Query("DELETE FROM task_assignments WHERE taskId = :taskId")
    suspend fun deleteAssignmentsByTaskId(taskId: String)
    
    @Query("DELETE FROM task_assignments WHERE memberId = :memberId")
    suspend fun deleteAssignmentsByMemberId(memberId: String)
}
