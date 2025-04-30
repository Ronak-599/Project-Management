import React, { useEffect } from 'react';
import { StyleSheet, View, ScrollView } from 'react-native';
import { Text, Card, Button, IconButton, ProgressBar, Surface } from 'react-native-paper';
import { useNavigation } from '@react-navigation/native';
import { ScreenNavigationProp } from '../../navigation/types';
import { useProjectStore } from '../../store/projectStore';
import { useTaskStore } from '../../store/taskStore';
import { TaskStatus } from '../../models/Task';
import ProjectCard from '../../components/ProjectCard';
import TaskItem from '../../components/TaskItem';
import { useAuth } from '../../context/AuthContext';

const DashboardScreen = () => {
  const navigation = useNavigation<ScreenNavigationProp<'Dashboard'>>();
  const { projects, fetchProjects } = useProjectStore();
  const { tasks, fetchTasks, updateTask } = useTaskStore();
  const { logout } = useAuth();

  useEffect(() => {
    fetchProjects();
    fetchTasks();
  }, [fetchProjects, fetchTasks]);

  // Get recent projects and upcoming tasks
  const recentProjects = projects.slice(0, 3);
  const upcomingTasks = tasks
    .filter(task => task.status !== TaskStatus.COMPLETED)
    .sort((a, b) => {
      if (!a.dueDate && !b.dueDate) return 0;
      if (!a.dueDate) return 1;
      if (!b.dueDate) return -1;
      return a.dueDate.getTime() - b.dueDate.getTime();
    })
    .slice(0, 4);

  // Calculate statistics
  const projectCount = projects.length;
  const taskCount = tasks.length;
  const completedTaskCount = tasks.filter(task => task.status === TaskStatus.COMPLETED).length;
  const inProgressTaskCount = tasks.filter(task => task.status === TaskStatus.IN_PROGRESS).length;
  const todoTaskCount = tasks.filter(task => task.status === TaskStatus.TODO).length;
  const completionRate = taskCount > 0 ? Math.round((completedTaskCount / taskCount) * 100) : 0;

  const handleTaskStatusChange = (task, newStatus) => {
    updateTask({ ...task, status: newStatus });
  };

  const navigateToProjectDetails = (projectId: string) => {
    navigation.navigate('ProjectDetails', { projectId });
  };

  return (
    <ScrollView style={styles.container}>
      <Surface style={styles.header}>
        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
          <View>
            <Text variant="headlineMedium" style={styles.title}>Dashboard</Text>
            <Text variant="bodyMedium" style={styles.subtitle}>
              Welcome to your project management dashboard
            </Text>
          </View>
        </View>
      </Surface>

      {/* Statistics Section */}
      <View style={styles.statsContainer}>
        <Card style={styles.statsCard}>
          <Card.Content>
            <Text variant="titleLarge" style={styles.statNumber}>{projectCount}</Text>
            <Text variant="bodyMedium">Projects</Text>
          </Card.Content>
        </Card>

        <Card style={styles.statsCard}>
          <Card.Content>
            <Text variant="titleLarge" style={styles.statNumber}>{taskCount}</Text>
            <Text variant="bodyMedium">Tasks</Text>
          </Card.Content>
        </Card>

        <Card style={styles.statsCard}>
          <Card.Content>
            <Text variant="titleLarge" style={styles.statNumber}>{completionRate}%</Text>
            <Text variant="bodyMedium">Completed</Text>
          </Card.Content>
        </Card>
      </View>

      {/* Task Status Section */}
      <View style={styles.taskStatusSection}>
        <Card style={styles.taskStatusCard}>
          <Card.Content>
            <Text variant="titleMedium" style={styles.sectionTitle}>Task Status</Text>
            
            <View style={styles.statusRow}>
              <View style={styles.statusItem}>
                <Text variant="bodySmall" style={styles.statusLabel}>To Do</Text>
                <Text variant="bodyLarge" style={styles.statusNumber}>{todoTaskCount}</Text>
              </View>
              
              <View style={styles.statusItem}>
                <Text variant="bodySmall" style={styles.statusLabel}>In Progress</Text>
                <Text variant="bodyLarge" style={styles.statusNumber}>{inProgressTaskCount}</Text>
              </View>
              
              <View style={styles.statusItem}>
                <Text variant="bodySmall" style={styles.statusLabel}>Completed</Text>
                <Text variant="bodyLarge" style={styles.statusNumber}>{completedTaskCount}</Text>
              </View>
            </View>

            <ProgressBar 
              progress={completionRate / 100} 
              color="#4CAF50" 
              style={styles.progressBar}
            />
          </Card.Content>
        </Card>
      </View>

      {/* Recent Projects Section */}
      <View style={styles.section}>
        <View style={styles.sectionHeader}>
          <Text variant="titleMedium" style={styles.sectionTitle}>Recent Projects</Text>
          <Button 
            mode="text" 
            onPress={() => navigation.navigate('Projects')}
          >
            View All
          </Button>
        </View>

        {recentProjects.length === 0 ? (
          <Card style={styles.emptyStateCard}>
            <Card.Content style={styles.emptyState}>
              <Text style={styles.emptyStateText}>
                No projects yet. Create your first project to get started!
              </Text>
              <Button 
                mode="contained" 
                onPress={() => navigation.navigate('Projects')}
                style={styles.emptyStateButton}
              >
                Create Project
              </Button>
            </Card.Content>
          </Card>
        ) : (
          recentProjects.map(project => (
            <ProjectCard
              key={project.id}
              project={project}
              onPress={navigateToProjectDetails}
              progress={0.5} // This would be calculated based on completed tasks
            />
          ))
        )}
      </View>

      {/* Upcoming Tasks Section */}
      <View style={styles.section}>
        <View style={styles.sectionHeader}>
          <Text variant="titleMedium" style={styles.sectionTitle}>Upcoming Tasks</Text>
          <Button 
            mode="text" 
            onPress={() => navigation.navigate('Tasks')}
          >
            View All
          </Button>
        </View>

        {upcomingTasks.length === 0 ? (
          <Card style={styles.emptyStateCard}>
            <Card.Content style={styles.emptyState}>
              <Text style={styles.emptyStateText}>
                No upcoming tasks. All caught up!
              </Text>
              <Button 
                mode="contained" 
                onPress={() => navigation.navigate('Tasks')}
                style={styles.emptyStateButton}
              >
                Create Task
              </Button>
            </Card.Content>
          </Card>
        ) : (
          upcomingTasks.map(task => (
            <TaskItem
              key={task.id}
              task={task}
              onPress={() => navigation.navigate('Tasks')}
              onStatusChange={handleTaskStatusChange}
            />
          ))
        )}
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F8F9FA',
  },
  header: {
    padding: 20,
    paddingTop: 60,
    paddingBottom: 40,
    backgroundColor: '#1976D2',
    borderBottomLeftRadius: 20,
    borderBottomRightRadius: 20,
    elevation: 4,
  },
  title: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 28,
  },
  subtitle: {
    color: 'rgba(255, 255, 255, 0.9)',
    marginTop: 6,
    fontSize: 16,
  },
  statsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: -30,
    paddingHorizontal: 16,
    marginBottom: 16,
  },
  statsCard: {
    flex: 1,
    margin: 4,
    elevation: 4,
    borderRadius: 12,
    borderColor: '#E0E0E0',
    borderWidth: 1,
    overflow: 'hidden',
  },
  statNumber: {
    fontWeight: 'bold',
    fontSize: 28,
    marginBottom: 4,
    color: '#1976D2',
  },
  taskStatusSection: {
    padding: 16,
    paddingTop: 0,
  },
  taskStatusCard: {
    borderRadius: 12,
    elevation: 2,
    borderColor: '#E0E0E0',
    borderWidth: 1,
    overflow: 'hidden',
  },
  statusRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 16,
  },
  statusItem: {
    alignItems: 'center',
    flex: 1,
  },
  statusLabel: {
    color: '#616161',
    marginBottom: 6,
    fontWeight: '500',
  },
  statusNumber: {
    fontWeight: 'bold',
    fontSize: 22,
    color: '#212121',
  },
  progressBar: {
    height: 8,
    borderRadius: 4,
    marginTop: 8,
  },
  section: {
    padding: 16,
    paddingTop: 8,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 12,
  },
  sectionTitle: {
    fontWeight: 'bold',
    fontSize: 18,
    color: '#212121',
  },
  emptyStateCard: {
    borderRadius: 12,
    marginBottom: 8,
    borderColor: '#E0E0E0',
    borderWidth: 1,
  },
  emptyState: {
    alignItems: 'center',
    padding: 24,
  },
  emptyStateText: {
    textAlign: 'center',
    color: '#616161',
    marginBottom: 16,
    fontSize: 15,
  },
  emptyStateButton: {
    borderRadius: 20,
    paddingHorizontal: 16,
  },
});

export default DashboardScreen; 