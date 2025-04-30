package com.example.pms.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // This module is kept for future use
    // All database and repository bindings are now in DatabaseModule and RepositoryModule
}
