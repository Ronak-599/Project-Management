import React, { useEffect, useState } from 'react';
import { View, StyleSheet, FlatList, RefreshControl } from 'react-native';
import { Text, FAB, ActivityIndicator, useTheme, Banner } from 'react-native-paper';
import { useNavigation } from '@react-navigation/native';
import { useTaskStore } from '../../store/taskStore';
import { Task, TaskStatus } from '../../models/Task';
import TaskListItem from '../../components/TaskListItem';
import TaskFilterHeader from '../../components/TaskFilterHeader';
import { ScreenNavigationProp } from '../../navigation/types';

const TasksScreen = () => {
  const navigation = useNavigation<ScreenNavigationProp<'Tasks'>>();
  const { tasks, loading, fetchTasks, updateTask, deleteTask } = useTaskStore();
  const theme = useTheme();
  const [searchQuery, setSearchQuery] = useState('');
  const [filteredTasks, setFilteredTasks] = useState<Task[]>([]);
  const [refreshing, setRefreshing] = useState(false);
  
  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  useEffect(() => {
    // Filter tasks based on search query
    if (searchQuery.trim() === '') {
      setFilteredTasks(tasks);
    } else {
      const filtered = tasks.filter(task => 
        task.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
        task.description.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setFilteredTasks(filtered);
    }
  }, [tasks, searchQuery]);

  const handleTaskPress = (taskId: string) => {
    // Navigate to task details
    console.log('Task pressed:', taskId);
  };

  const handleDeleteTask = (taskId: string) => {
    deleteTask(taskId);
  };

  const handleFilterByStatus = () => {
    console.log('Filter by status pressed');
    // Implement status filter logic here
  };

  const handleFilterByPriority = () => {
    console.log('Filter by priority pressed');
    // Implement priority filter logic here
  };

  const handleFilterByProject = () => {
    console.log('Filter by project pressed');
    // Implement project filter logic here
  };

  const handleAddTask = () => {
    navigation.navigate('AddTask');
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await fetchTasks();
    setRefreshing(false);
  };

  if (loading && !refreshing) {
    return (
      <View style={[styles.loadingContainer, { backgroundColor: theme.colors.background }]}>
        <ActivityIndicator size="large" color={theme.colors.primary} />
      </View>
    );
  }

  const getTasksHeader = () => {
    return (
      <>
        <View style={styles.headerContainer}>
          <Text variant="headlineMedium" style={styles.screenTitle}>Tasks</Text>
          <Text variant="bodyLarge" style={styles.taskCount}>
            {filteredTasks.length} {filteredTasks.length === 1 ? 'task' : 'tasks'}
          </Text>
        </View>
        <TaskFilterHeader 
          searchQuery={searchQuery}
          onSearchChange={setSearchQuery}
          onFilterByStatus={handleFilterByStatus}
          onFilterByPriority={handleFilterByPriority}
          onFilterByProject={handleFilterByProject}
        />
      </>
    );
  };

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <FlatList
        data={filteredTasks}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <TaskListItem
            task={item}
            onPress={handleTaskPress}
            onDelete={handleDeleteTask}
          />
        )}
        ListHeaderComponent={getTasksHeader()}
        ListEmptyComponent={
          !loading && (
            <View style={styles.emptyContainer}>
              <Text style={[styles.emptyText, { color: theme.colors.onSurfaceVariant }]}>
                No tasks found
              </Text>
              <Text style={[styles.emptySubText, { color: theme.colors.onSurfaceVariant }]}>
                {searchQuery ? 'Try a different search term' : 'Tap the + button to add a new task'}
              </Text>
            </View>
          )
        }
        contentContainerStyle={[
          styles.listContent,
          filteredTasks.length === 0 && styles.emptyListContent
        ]}
        refreshControl={
          <RefreshControl 
            refreshing={refreshing} 
            onRefresh={onRefresh}
            colors={[theme.colors.primary]}
            tintColor={theme.colors.primary}
          />
        }
      />
      <FAB
        style={[styles.fab, { backgroundColor: theme.colors.primary }]}
        color={theme.colors.onPrimary}
        icon="plus"
        onPress={handleAddTask}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 8,
  },
  headerContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'baseline',
    paddingHorizontal: 16,
    paddingTop: 12,
    paddingBottom: 4,
  },
  screenTitle: {
    fontWeight: 'bold',
  },
  taskCount: {
    opacity: 0.7,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  listContent: {
    paddingHorizontal: 16,
    paddingBottom: 90, // Extra space for FAB
  },
  emptyListContent: {
    flex: 1,
    justifyContent: 'center',
  },
  emptyContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    padding: 40,
  },
  emptyText: {
    fontSize: 18,
    fontWeight: '500',
    marginBottom: 8,
  },
  emptySubText: {
    fontSize: 14,
    textAlign: 'center',
  },
  fab: {
    position: 'absolute',
    margin: 16,
    right: 0,
    bottom: 0,
    borderRadius: 30,
    elevation: 4,
  },
});

export default TasksScreen; 