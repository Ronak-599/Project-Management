package com.example.pms.di

import android.content.Context
import com.example.pms.data.database.AppDatabase
import com.example.pms.data.database.dao.ProjectDao
import com.example.pms.data.database.dao.TaskAssignmentDao
import com.example.pms.data.database.dao.TaskDao
import com.example.pms.data.database.dao.TeamMemberDao
import com.example.pms.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun provideProjectDao(database: AppDatabase): ProjectDao {
        return database.projectDao()
    }
    
    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }
    
    @Provides
    @Singleton
    fun provideTeamMemberDao(database: AppDatabase): TeamMemberDao {
        return database.teamMemberDao()
    }
    
    @Provides
    @Singleton
    fun provideTaskAssignmentDao(database: AppDatabase): TaskAssignmentDao {
        return database.taskAssignmentDao()
    }
    
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}
