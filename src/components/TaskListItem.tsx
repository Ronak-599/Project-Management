import React from 'react';
import { StyleSheet, View, TouchableOpacity } from 'react-native';
import { Card, Text, Badge, IconButton, useTheme } from 'react-native-paper';
import { format } from 'date-fns';
import { Task, TaskStatus, TaskPriority } from '../models/Task';
import { SAMPLE_PROJECTS } from '../utils/SampleData';

type TaskListItemProps = {
  task: Task;
  onPress: (taskId: string) => void;
  onDelete: (taskId: string) => void;
};

const getPriorityLabel = (priority: TaskPriority): string => {
  switch (priority) {
    case TaskPriority.HIGH:
      return 'HIGH';
    case TaskPriority.MEDIUM:
      return 'MEDIUM';
    case TaskPriority.LOW:
      return 'LOW';
    default:
      return 'Unknown';
  }
};

const getStatusLabel = (status: TaskStatus): string => {
  switch (status) {
    case TaskStatus.COMPLETED:
      return 'COMPLETED';
    case TaskStatus.IN_PROGRESS:
      return 'IN PROGRESS';
    case TaskStatus.TODO:
      return 'TODO';
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

const TaskListItem: React.FC<TaskListItemProps> = ({ 
  task, 
  onPress, 
  onDelete 
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
        return theme.colors.surfaceVariant;
    }
  };

  const getStatusColor = (status: TaskStatus): string => {
    switch (status) {
      case TaskStatus.COMPLETED:
        return '#4CAF50'; // Green
      case TaskStatus.IN_PROGRESS:
        return '#2196F3'; // Blue
      case TaskStatus.TODO:
        return '#9E9E9E'; // Gray
      default:
        return theme.colors.surfaceVariant;
    }
  };

  const getProjectText = () => {
    const project = SAMPLE_PROJECTS.find(p => p.id === task.projectId);
    return project ? project.name : 'Unknown Project';
  };

  const isDueDatePassed = () => {
    return task.dueDate && task.dueDate < new Date() && task.status !== TaskStatus.COMPLETED;
  };

  return (
    <Card 
      style={[
        styles.card, 
        { 
          backgroundColor: theme.colors.surface,
          borderLeftColor: getPriorityColor(task.priority),
          borderLeftWidth: 4,
        }
      ]}
      mode="outlined"
    >
      <TouchableOpacity onPress={() => onPress(task.id)} style={styles.touchable}>
        <View style={styles.content}>
          <View style={styles.headerRow}>
            <View style={styles.statusIconContainer}>
              <IconButton
                icon={getStatusIcon(task.status)}
                size={22}
                iconColor={getStatusColor(task.status)}
                style={[styles.statusIcon, { backgroundColor: `${getStatusColor(task.status)}15` }]}
              />
            </View>
            
            <View style={styles.titleContainer}>
              <Text style={[
                styles.title,
                task.status === TaskStatus.COMPLETED && styles.completedText
              ]}>
                {task.title}
              </Text>
              <Text style={styles.projectText}>{getProjectText()}</Text>
            </View>
            
            <IconButton
              icon="trash-can-outline"
              size={20}
              iconColor={theme.colors.error}
              onPress={() => onDelete(task.id)}
              style={styles.deleteButton}
            />
          </View>
          
          {task.description && (
            <Text 
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
            <View style={styles.badges}>
              <View style={[
                styles.statusBadge,
                { backgroundColor: `${getStatusColor(task.status)}15` }
              ]}>
                <Text style={[styles.statusText, { color: getStatusColor(task.status) }]}>
                  {getStatusLabel(task.status)}
                </Text>
              </View>
              
              <View style={[
                styles.priorityBadge, 
                { backgroundColor: `${getPriorityColor(task.priority)}15` }
              ]}>
                <Text style={[styles.priorityText, { color: getPriorityColor(task.priority) }]}>
                  {getPriorityLabel(task.priority)}
                </Text>
              </View>
            </View>
            
            {task.dueDate && (
              <Text style={[
                styles.dateText,
                isDueDatePassed() && styles.overdueDateText
              ]}>
                {isDueDatePassed() && <Text style={styles.overdueLabel}>OVERDUE: </Text>}
                {format(task.dueDate, 'MMM d, yyyy')}
              </Text>
            )}
          </View>
        </View>
      </TouchableOpacity>
    </Card>
  );
};

const styles = StyleSheet.create({
  card: {
    marginBottom: 12,
    borderRadius: 12,
    overflow: 'hidden',
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  touchable: {
    width: '100%',
  },
  content: {
    padding: 16,
  },
  headerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  statusIconContainer: {
    marginRight: 8,
  },
  statusIcon: {
    margin: 0,
    borderRadius: 20,
  },
  titleContainer: {
    flex: 1,
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 2,
  },
  completedText: {
    textDecorationLine: 'line-through',
    opacity: 0.7,
  },
  projectText: {
    color: '#2196F3',
    fontSize: 13,
  },
  deleteButton: {
    margin: 0,
    padding: 0,
  },
  description: {
    color: '#616161',
    marginBottom: 16,
    fontSize: 14,
    lineHeight: 20,
  },
  footer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
  },
  badges: {
    flexDirection: 'row',
    gap: 8,
  },
  priorityBadge: {
    paddingHorizontal: 10,
    paddingVertical: 5,
    borderRadius: 16,
  },
  priorityText: {
    fontWeight: '600',
    fontSize: 11,
  },
  statusBadge: {
    paddingHorizontal: 10,
    paddingVertical: 5,
    borderRadius: 16,
  },
  statusText: {
    fontSize: 11,
    fontWeight: '600',
  },
  dateText: {
    color: '#616161',
    fontSize: 13,
    fontWeight: '500',
  },
  overdueDateText: {
    color: '#F44336',
  },
  overdueLabel: {
    fontWeight: 'bold',
  }
});

export default TaskListItem; 