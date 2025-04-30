import React from 'react';
import { StyleSheet, View, TouchableOpacity } from 'react-native';
import { Card, Text, Chip, IconButton, useTheme } from 'react-native-paper';
import { format } from 'date-fns';
import { Task, TaskStatus, TaskPriority } from '../models/Task';

type TaskItemProps = {
  task: Task;
  onPress: (taskId: string) => void;
  onStatusChange?: (task: Task, newStatus: TaskStatus) => void;
};

const getPriorityLabel = (priority: TaskPriority): string => {
  switch (priority) {
    case TaskPriority.HIGH:
      return 'High';
    case TaskPriority.MEDIUM:
      return 'Medium';
    case TaskPriority.LOW:
      return 'Low';
    default:
      return 'Unknown';
  }
};

const getStatusIcon = (status: TaskStatus): string => {
  switch (status) {
    case TaskStatus.COMPLETED:
      return 'check-circle';
    case TaskStatus.IN_PROGRESS:
      return 'progress-clock';
    case TaskStatus.TODO:
      return 'circle-outline';
    default:
      return 'circle-outline';
  }
};

const getNextStatus = (status: TaskStatus): TaskStatus => {
  switch (status) {
    case TaskStatus.TODO:
      return TaskStatus.IN_PROGRESS;
    case TaskStatus.IN_PROGRESS:
      return TaskStatus.COMPLETED;
    case TaskStatus.COMPLETED:
      return TaskStatus.TODO;
    default:
      return TaskStatus.TODO;
  }
};

const TaskItem: React.FC<TaskItemProps> = ({ 
  task, 
  onPress, 
  onStatusChange 
}) => {
  const theme = useTheme();
  
  const getPriorityColor = (priority: TaskPriority): string => {
    switch (priority) {
      case TaskPriority.HIGH:
        return '#F44336'; // Red
      case TaskPriority.MEDIUM:
        return '#FF9800'; // Orange
      case TaskPriority.LOW:
        return '#4CAF50'; // Green
      default:
        return theme.colors.onSurfaceVariant;
    }
  };

  const getStatusColor = (status: TaskStatus): string => {
    switch (status) {
      case TaskStatus.COMPLETED:
        return '#4CAF50'; // Green
      case TaskStatus.IN_PROGRESS:
        return '#2196F3'; // Blue
      case TaskStatus.TODO:
        return theme.colors.onSurfaceVariant;
      default:
        return theme.colors.onSurfaceVariant;
    }
  };

  const handleStatusPress = () => {
    if (onStatusChange) {
      const newStatus = getNextStatus(task.status);
      onStatusChange(task, newStatus);
    }
  };

  const isDueDatePassed = () => {
    return task.dueDate && task.dueDate < new Date() && task.status !== TaskStatus.COMPLETED;
  };

  return (
    <Card style={[
      styles.card, 
      { 
        backgroundColor: theme.colors.surface,
        borderLeftColor: getPriorityColor(task.priority),
        borderLeftWidth: 3
      }
    ]}>
      <TouchableOpacity onPress={() => onPress(task.id)} style={styles.touchable}>
        <Card.Content style={styles.content}>
          <View style={styles.header}>
            <IconButton
              icon={getStatusIcon(task.status)}
              size={24}
              iconColor={getStatusColor(task.status)}
              onPress={handleStatusPress}
              style={[styles.statusIcon, { backgroundColor: `${getStatusColor(task.status)}15` }]}
            />
            
            <View style={styles.titleContainer}>
              <Text 
                variant="titleMedium" 
                style={[
                  styles.title,
                  task.status === TaskStatus.COMPLETED && styles.completedText
                ]}
              >
                {task.title}
              </Text>
            </View>
          </View>
          
          {task.description && (
            <Text 
              variant="bodyMedium" 
              style={[
                styles.description,
                task.status === TaskStatus.COMPLETED && styles.completedText
              ]} 
              numberOfLines={2}
            >
              {task.description}
            </Text>
          )}
          
          <View style={styles.footer}>
            <Chip 
              style={[styles.priorityChip, { backgroundColor: `${getPriorityColor(task.priority)}15` }]}
              textStyle={{ color: getPriorityColor(task.priority), fontWeight: '600', fontSize: 12 }}
              compact
            >
              {getPriorityLabel(task.priority)}
            </Chip>
            
            {task.dueDate && (
              <View style={styles.dueDateContainer}>
                <Text 
                  variant="bodySmall" 
                  style={[
                    styles.date, 
                    isDueDatePassed() && styles.overdueDate
                  ]}
                >
                  {isDueDatePassed() && <Text variant="bodySmall" style={styles.overdueText}>OVERDUE: </Text>}
                  {format(task.dueDate, 'MMM d, yyyy')}
                </Text>
              </View>
            )}
          </View>
        </Card.Content>
      </TouchableOpacity>
    </Card>
  );
};

const styles = StyleSheet.create({
  card: {
    marginBottom: 12,
    borderRadius: 12,
    elevation: 2,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
  },
  touchable: {
    width: '100%',
  },
  content: {
    paddingVertical: 14,
    paddingHorizontal: 16,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  statusIcon: {
    margin: 0,
    marginRight: 12,
    borderRadius: 20,
  },
  titleContainer: {
    flex: 1,
  },
  title: {
    fontWeight: '700',
    fontSize: 16,
  },
  description: {
    color: '#616161',
    marginBottom: 12,
    lineHeight: 20,
    marginLeft: 36, // To align with the title
  },
  completedText: {
    textDecorationLine: 'line-through',
    opacity: 0.7,
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginLeft: 36, // To align with the description
  },
  priorityChip: {
    height: 28,
    borderRadius: 14,
  },
  dueDateContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  date: {
    fontWeight: '500',
    color: '#616161',
    fontSize: 13,
  },
  overdueDate: {
    color: '#F44336',
  },
  overdueText: {
    fontWeight: 'bold',
  }
});

export default TaskItem; 