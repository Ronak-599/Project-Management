package com.example.pms.data.database.converters

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converter for Room to convert between Date and Long
 * This allows Room to store Date objects in the database
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
