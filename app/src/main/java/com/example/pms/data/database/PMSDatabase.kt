package com.example.pms.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pms.data.database.dao.ProjectDao
import com.example.pms.data.database.dao.TaskAssignmentDao
import com.example.pms.data.database.dao.TaskDao
import com.example.pms.data.database.dao.TeamMemberDao
import com.example.pms.data.database.entities.ProjectEntity
import com.example.pms.data.database.entities.TaskAssignmentEntity
import com.example.pms.data.database.entities.TaskEntity
import com.example.pms.data.database.entities.TeamMemberEntity

@Database(
    entities = [
        ProjectEntity::class,
        TaskEntity::class,
        TeamMemberEntity::class,
        TaskAssignmentEntity::class
    ],
    version = 2, // Increased from 1 to match AppDatabase version
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PMSDatabase : RoomDatabase() {
    
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun taskAssignmentDao(): TaskAssignmentDao
    
    companion object {
        @Volatile
        private var INSTANCE: PMSDatabase? = null
        
        fun getDatabase(context: Context): PMSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PMSDatabase::class.java,
                    "pms_database"
                )
                .fallbackToDestructiveMigration() // This will drop and recreate the database if migration isn't possible
                .allowMainThreadQueries() // Allow queries on main thread for simplicity (remove in production)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
