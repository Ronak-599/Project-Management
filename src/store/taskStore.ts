import { create } from 'zustand';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Task, TaskStatus, TaskPriority, createTask } from '../models/Task';
import { SAMPLE_TASKS } from '../utils/SampleData';

interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  
  // Actions
  addTask: (projectId: string, title: string, description: string, status?: TaskStatus, priority?: TaskPriority, dueDate?: Date | null) => Promise<Task>;
  updateTask: (task: Task) => Promise<void>;
  deleteTask: (id: string) => Promise<void>;
  getTaskById: (id: string) => Task | undefined;
  getTasksByProjectId: (projectId: string) => Task[];
  fetchTasks: () => Promise<void>;
}

export const useTaskStore = create<TaskState>((set, get) => ({
  tasks: [],
  loading: false,
  error: null,

  addTask: async (projectId, title, description, status = TaskStatus.TODO, priority = TaskPriority.MEDIUM, dueDate = null) => {
    const newTask = createTask(projectId, title, description, status, priority, dueDate);
    
    set((state) => ({ 
      tasks: [...state.tasks, newTask] 
    }));
    
    // Save to AsyncStorage
    await saveTasks([...get().tasks]);
    
    return newTask;
  },

  updateTask: async (updatedTask) => {
    set((state) => ({
      tasks: state.tasks.map(task => 
        task.id === updatedTask.id ? updatedTask : task
      )
    }));
    
    // Save to AsyncStorage
    await saveTasks(get().tasks);
  },

  deleteTask: async (id) => {
    set((state) => ({
      tasks: state.tasks.filter(task => task.id !== id)
    }));
    
    // Save to AsyncStorage
    await saveTasks(get().tasks);
  },

  getTaskById: (id) => {
    return get().tasks.find(task => task.id === id);
  },

  getTasksByProjectId: (projectId) => {
    return get().tasks.filter(task => task.projectId === projectId);
  },

  fetchTasks: async () => {
    set({ loading: true, error: null });
    
    try {
      const tasks = await loadTasks();
      set({ tasks, loading: false });
    } catch (error) {
      set({ 
        error: error instanceof Error ? error.message : 'Failed to load tasks', 
        loading: false 
      });
    }
  }
}));

// Helper functions for AsyncStorage
const STORAGE_KEY = '@tasks';

const saveTasks = async (tasks: Task[]) => {
  try {
    // Convert Date objects to strings for storage
    const tasksToSave = tasks.map(task => ({
      ...task,
      dueDate: task.dueDate ? task.dueDate.toISOString() : null,
      createdAt: task.createdAt.toISOString()
    }));
    
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(tasksToSave));
  } catch (error) {
    console.error('Error saving tasks:', error);
    throw error;
  }
};

const loadTasks = async (): Promise<Task[]> => {
  try {
    const data = await AsyncStorage.getItem(STORAGE_KEY);
    if (!data) {
      // If no tasks exist in storage, return sample data
      return SAMPLE_TASKS;
    }
    
    // Convert string dates back to Date objects
    const tasks = JSON.parse(data);
    return tasks.map((task: any) => ({
      ...task,
      dueDate: task.dueDate ? new Date(task.dueDate) : null,
      createdAt: new Date(task.createdAt)
    }));
  } catch (error) {
    console.error('Error loading tasks:', error);
    return SAMPLE_TASKS; // Return sample data if there's an error
  }
}; 