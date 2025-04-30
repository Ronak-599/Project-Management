package com.example.pms.di

import com.example.pms.data.repository.ProjectRepository
import com.example.pms.data.repository.RoomProjectRepository
import com.example.pms.data.repository.RoomTaskAssignmentRepository
import com.example.pms.data.repository.RoomTaskRepository
import com.example.pms.data.repository.RoomTeamMemberRepository
import com.example.pms.data.repository.RoomUserRepository
import com.example.pms.data.repository.TaskAssignmentRepository
import com.example.pms.data.repository.TaskRepository
import com.example.pms.data.repository.TeamMemberRepository
import com.example.pms.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        repository: RoomUserRepository
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindProjectRepository(
        repository: RoomProjectRepository
    ): ProjectRepository
    
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        repository: RoomTaskRepository
    ): TaskRepository
    
    @Binds
    @Singleton
    abstract fun bindTeamMemberRepository(
        repository: RoomTeamMemberRepository
    ): TeamMemberRepository
    
    @Binds
    @Singleton
    abstract fun bindTaskAssignmentRepository(
        repository: RoomTaskAssignmentRepository
    ): TaskAssignmentRepository
}
