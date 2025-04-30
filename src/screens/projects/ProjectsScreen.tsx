import React, { useEffect, useState } from 'react';
import { StyleSheet, View, ScrollView, FlatList } from 'react-native';
import { 
  Text, 
  FAB, 
  Dialog, 
  Portal, 
  Button, 
  TextInput, 
  Surface,
  SegmentedButtons
} from 'react-native-paper';
import { useNavigation } from '@react-navigation/native';
import { ScreenNavigationProp } from '../../navigation/types';
import { useProjectStore } from '../../store/projectStore';
import { useTaskStore } from '../../store/taskStore';
import { Project, ProjectStatus } from '../../models/Project';
import ProjectCard from '../../components/ProjectCard';
import DatePickerField from '../../components/DatePickerField';

const ProjectsScreen = () => {
  const navigation = useNavigation<ScreenNavigationProp<'Projects'>>();
  const { projects, fetchProjects, addProject } = useProjectStore();
  const { tasks } = useTaskStore();
  
  const [dialogVisible, setDialogVisible] = useState(false);
  const [projectName, setProjectName] = useState('');
  const [projectDescription, setProjectDescription] = useState('');
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchProjects();
  }, [fetchProjects]);

  const showDialog = () => setDialogVisible(true);
  const hideDialog = () => setDialogVisible(false);

  const handleCreateProject = async () => {
    if (!projectName.trim()) return;
    
    await addProject(projectName, projectDescription, startDate, endDate);
    
    // Reset form
    setProjectName('');
    setProjectDescription('');
    setStartDate(null);
    setEndDate(null);
    hideDialog();
  };

  const handleProjectPress = (projectId: string) => {
    navigation.navigate('ProjectDetails', { projectId });
  };

  const getFilteredProjects = () => {
    switch (filter) {
      case 'active':
        return projects.filter(p => 
          p.status === ProjectStatus.IN_PROGRESS || 
          p.status === ProjectStatus.NOT_STARTED
        );
      case 'completed':
        return projects.filter(p => p.status === ProjectStatus.COMPLETED);
      case 'all':
      default:
        return projects;
    }
  };

  const getProjectProgress = (projectId: string): number => {
    const projectTasks = tasks.filter(task => task.projectId === projectId);
    if (projectTasks.length === 0) return 0;
    
    const completedTasks = projectTasks.filter(task => task.status === 'COMPLETED').length;
    return completedTasks / projectTasks.length;
  };

  const filteredProjects = getFilteredProjects();

  return (
    <View style={styles.container}>
      <Surface style={styles.header}>
        <Text variant="headlineMedium" style={styles.title}>Projects</Text>
      </Surface>

      <View style={styles.filterContainer}>
        <SegmentedButtons
          value={filter}
          onValueChange={setFilter}
          buttons={[
            { value: 'all', label: 'All' },
            { value: 'active', label: 'Active' },
            { value: 'completed', label: 'Completed' }
          ]}
          style={styles.segmentedButtons}
        />
      </View>

      {filteredProjects.length === 0 ? (
        <View style={styles.emptyContainer}>
          <Text variant="bodyLarge" style={styles.emptyText}>
            No projects found. Create your first project!
          </Text>
          <Button mode="contained" onPress={showDialog} style={styles.emptyButton}>
            Create Project
          </Button>
        </View>
      ) : (
        <FlatList
          data={filteredProjects}
          keyExtractor={item => item.id}
          renderItem={({ item }) => (
            <ProjectCard
              project={item}
              onPress={handleProjectPress}
              progress={getProjectProgress(item.id)}
            />
          )}
          contentContainerStyle={styles.listContent}
        />
      )}

      <Portal>
        <Dialog visible={dialogVisible} onDismiss={hideDialog}>
          <Dialog.Title>Create New Project</Dialog.Title>
          <Dialog.Content>
            <TextInput
              label="Project Name"
              value={projectName}
              onChangeText={setProjectName}
              style={styles.input}
            />
            <TextInput
              label="Description"
              value={projectDescription}
              onChangeText={setProjectDescription}
              multiline
              numberOfLines={3}
              style={styles.input}
            />
            <DatePickerField
              label="Start Date"
              date={startDate}
              onDateChange={setStartDate}
              style={styles.input}
            />
            <DatePickerField
              label="End Date"
              date={endDate}
              onDateChange={setEndDate}
              style={styles.input}
              minimumDate={startDate}
            />
          </Dialog.Content>
          <Dialog.Actions>
            <Button onPress={hideDialog}>Cancel</Button>
            <Button onPress={handleCreateProject} mode="contained">Create</Button>
          </Dialog.Actions>
        </Dialog>
      </Portal>

      <FAB
        icon="plus"
        style={styles.fab}
        onPress={showDialog}
        color="white"
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F5F5',
  },
  header: {
    padding: 20,
    paddingTop: 60,
    backgroundColor: '#2196F3',
  },
  title: {
    color: 'white',
    fontWeight: 'bold',
  },
  filterContainer: {
    padding: 16,
    paddingBottom: 8,
  },
  segmentedButtons: {
    backgroundColor: 'white',
  },
  listContent: {
    padding: 16,
    paddingTop: 8,
    paddingBottom: 80,
  },
  fab: {
    position: 'absolute',
    margin: 16,
    right: 0,
    bottom: 0,
    backgroundColor: '#2196F3',
  },
  input: {
    marginBottom: 16,
    backgroundColor: 'transparent',
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },
  emptyText: {
    textAlign: 'center',
    marginBottom: 16,
    color: '#757575',
  },
  emptyButton: {
    borderRadius: 20,
  },
});

export default ProjectsScreen; 