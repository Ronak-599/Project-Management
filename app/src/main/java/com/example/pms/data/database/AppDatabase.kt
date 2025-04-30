package com.example.pms.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pms.data.database.converters.DateConverter
import com.example.pms.data.database.dao.ProjectDao
import com.example.pms.data.database.dao.TaskAssignmentDao
import com.example.pms.data.database.dao.TaskDao
import com.example.pms.data.database.dao.TeamMemberDao
import com.example.pms.data.database.dao.UserDao
import com.example.pms.data.database.entities.ProjectEntity
import com.example.pms.data.database.entities.TaskAssignmentEntity
import com.example.pms.data.database.entities.TaskEntity
import com.example.pms.data.database.entities.TeamMemberEntity
import com.example.pms.data.database.entities.UserEntity

@Database(
    entities = [
        ProjectEntity::class,
        TaskEntity::class,
        TeamMemberEntity::class,
        TaskAssignmentEntity::class,
        UserEntity::class
    ],
    version = 2, // Increased from 1 to handle schema changes
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun taskAssignmentDao(): TaskAssignmentDao
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        private const val PREF_NAME = "database_prefs"
        private const val KEY_FIRST_RUN = "first_run_v2"
        
        fun getDatabase(context: Context): AppDatabase {
            // Check if this is the first run after update
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true)
            
            return INSTANCE ?: synchronized(this) {
                // If it's the first run, delete the database file to ensure clean slate
                if (isFirstRun) {
                    context.deleteDatabase("pms_database")
                    prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply()
                    println("AppDatabase: Deleted database for clean start")
                }
                
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pms_database"
                )
                .fallbackToDestructiveMigration() // This will drop and recreate the database if migration isn't possible
                .allowMainThreadQueries() // Allow queries on main thread for simplicity (remove in production)
                .build()
                
                println("AppDatabase: Database initialized successfully")
                INSTANCE = instance
                instance
            }
        }
    }
}
