package com.example.pms.data.database

import androidx.room.TypeConverter
import com.example.pms.data.models.ProjectStatus
import com.example.pms.data.models.TaskPriority
import com.example.pms.data.models.TaskStatus
import java.util.Date

/**
 * Type converters for Room database to handle complex data types
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromProjectStatus(status: ProjectStatus): String {
        return status.name
    }
    
    @TypeConverter
    fun toProjectStatus(status: String): ProjectStatus {
        return ProjectStatus.valueOf(status)
    }
    
    @TypeConverter
    fun fromTaskStatus(status: TaskStatus): String {
        return status.name
    }
    
    @TypeConverter
    fun toTaskStatus(status: String): TaskStatus {
        return TaskStatus.valueOf(status)
    }
    
    @TypeConverter
    fun fromTaskPriority(priority: TaskPriority): String {
        return priority.name
    }
    
    @TypeConverter
    fun toTaskPriority(priority: String): TaskPriority {
        return TaskPriority.valueOf(priority)
    }
}
