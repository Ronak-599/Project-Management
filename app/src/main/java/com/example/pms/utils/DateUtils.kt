package com.example.pms.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility class for date operations
 */
object DateUtils {
    /**
     * Format a date to a readable string
     * @param date The date to format, can be null
     * @return Formatted date string or "Not set" if date is null
     */
    fun formatDate(date: Date?): String {
        if (date == null) return "Not set"
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(date)
    }
    
    /**
     * Check if a task is overdue based on its due date and status
     * @param dueDate The task's due date
     * @param status The task's current status
     * @return True if the task is overdue, false otherwise
     */
    fun isOverdue(dueDate: Date?, status: com.example.pms.data.models.TaskStatus): Boolean {
        if (dueDate == null) return false
        if (status == com.example.pms.data.models.TaskStatus.COMPLETED) return false
        return dueDate.before(Date())
    }
}
