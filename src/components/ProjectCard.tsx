import React from 'react';
import { StyleSheet, View, TouchableOpacity } from 'react-native';
import { Card, Text, Chip, ProgressBar, useTheme } from 'react-native-paper';
import { format } from 'date-fns';
import { Project, ProjectStatus } from '../models/Project';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

type ProjectCardProps = {
  project: Project;
  onPress: (projectId: string) => void;
  progress?: number;
};

const getStatusColor = (status: ProjectStatus): string => {
  switch (status) {
    case ProjectStatus.COMPLETED:
      return '#4CAF50';
    case ProjectStatus.IN_PROGRESS:
      return '#2196F3';
    case ProjectStatus.ON_HOLD:
      return '#FF9800';
    case ProjectStatus.NOT_STARTED:
      return '#9E9E9E';
    case ProjectStatus.CANCELLED:
      return '#F44336';
    default:
      return '#9E9E9E';
  }
};

const getStatusLabel = (status: ProjectStatus): string => {
  switch (status) {
    case ProjectStatus.COMPLETED:
      return 'Completed';
    case ProjectStatus.IN_PROGRESS:
      return 'In Progress';
    case ProjectStatus.ON_HOLD:
      return 'On Hold';
    case ProjectStatus.NOT_STARTED:
      return 'Not Started';
    case ProjectStatus.CANCELLED:
      return 'Cancelled';
    default:
      return 'Unknown';
  }
};

const getStatusIcon = (status: ProjectStatus): string => {
  switch (status) {
    case ProjectStatus.COMPLETED:
      return 'check-circle';
    case ProjectStatus.IN_PROGRESS:
      return 'progress-clock';
    case ProjectStatus.ON_HOLD:
      return 'pause-circle';
    case ProjectStatus.NOT_STARTED:
      return 'circle-outline';
    case ProjectStatus.CANCELLED:
      return 'close-circle';
    default:
      return 'help-circle';
  }
};

const ProjectCard: React.FC<ProjectCardProps> = ({ project, onPress, progress = 0 }) => {
  const theme = useTheme();
  
  return (
    <TouchableOpacity onPress={() => onPress(project.id)} style={styles.touchable}>
      <Card 
        style={[styles.card, { 
          backgroundColor: theme.colors.surface,
          borderLeftColor: getStatusColor(project.status),
          borderLeftWidth: 3
        }]}
      >
        <Card.Content style={styles.content}>
          <View style={styles.header}>
            <View style={styles.titleContainer}>
              <Text variant="titleMedium" style={styles.title}>{project.name}</Text>
              
              <View style={styles.statusContainer}>
                <Icon 
                  name={getStatusIcon(project.status)} 
                  size={16} 
                  color={getStatusColor(project.status)} 
                  style={styles.statusIcon}
                />
                <Text 
                  variant="bodySmall" 
                  style={[styles.statusText, { color: getStatusColor(project.status) }]}
                >
                  {getStatusLabel(project.status)}
                </Text>
              </View>
            </View>
            
            {project.startDate && (
              <Text variant="bodySmall" style={styles.date}>
                {format(project.startDate, 'MMM d, yyyy')}
              </Text>
            )}
          </View>
          
          <Text variant="bodyMedium" style={styles.description} numberOfLines={2}>
            {project.description}
          </Text>
          
          <View style={styles.progressContainer}>
            <View style={styles.progressLabelContainer}>
              <Text variant="bodySmall" style={styles.progressLabel}>Progress</Text>
              <Text variant="bodySmall" style={styles.progressText}>
                {Math.round(progress * 100)}%
              </Text>
            </View>
            <ProgressBar 
              progress={progress} 
              color={getStatusColor(project.status)} 
              style={styles.progressBar} 
            />
          </View>
        </Card.Content>
      </Card>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  touchable: {
    marginBottom: 12,
    width: '100%',
  },
  card: {
    borderRadius: 12,
    elevation: 2,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
  },
  content: {
    padding: 16,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 12,
  },
  titleContainer: {
    flex: 1,
    marginRight: 16,
  },
  title: {
    fontWeight: '700',
    marginBottom: 6,
    fontSize: 16,
  },
  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  statusIcon: {
    marginRight: 4,
  },
  statusText: {
    fontWeight: '600',
    fontSize: 12,
  },
  description: {
    color: '#616161',
    marginBottom: 16,
    lineHeight: 20,
  },
  date: {
    color: '#616161',
    fontWeight: '500',
  },
  progressContainer: {
    marginTop: 4,
  },
  progressLabelContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  progressLabel: {
    fontSize: 12,
    color: '#616161',
    fontWeight: '500',
  },
  progressBar: {
    height: 8,
    borderRadius: 4,
  },
  progressText: {
    color: '#616161',
    fontWeight: '600',
    fontSize: 12,
  },
});

export default ProjectCard; 