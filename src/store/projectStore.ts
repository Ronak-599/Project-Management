import { create } from 'zustand';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Project, ProjectStatus, createProject } from '../models/Project';

interface ProjectState {
  projects: Project[];
  loading: boolean;
  error: string | null;
  
  // Actions
  addProject: (name: string, description: string, startDate?: Date | null, endDate?: Date | null) => Promise<Project>;
  updateProject: (project: Project) => Promise<void>;
  deleteProject: (id: string) => Promise<void>;
  getProjectById: (id: string) => Project | undefined;
  fetchProjects: () => Promise<void>;
}

export const useProjectStore = create<ProjectState>((set, get) => ({
  projects: [],
  loading: false,
  error: null,

  addProject: async (name, description, startDate, endDate) => {
    const newProject = createProject(name, description, startDate, endDate);
    
    set((state) => ({ 
      projects: [...state.projects, newProject] 
    }));
    
    // Save to AsyncStorage
    await saveProjects([...get().projects]);
    
    return newProject;
  },

  updateProject: async (updatedProject) => {
    set((state) => ({
      projects: state.projects.map(project => 
        project.id === updatedProject.id ? updatedProject : project
      )
    }));
    
    // Save to AsyncStorage
    await saveProjects(get().projects);
  },

  deleteProject: async (id) => {
    set((state) => ({
      projects: state.projects.filter(project => project.id !== id)
    }));
    
    // Save to AsyncStorage
    await saveProjects(get().projects);
  },

  getProjectById: (id) => {
    return get().projects.find(project => project.id === id);
  },

  fetchProjects: async () => {
    set({ loading: true, error: null });
    
    try {
      const projects = await loadProjects();
      set({ projects, loading: false });
    } catch (error) {
      set({ 
        error: error instanceof Error ? error.message : 'Failed to load projects', 
        loading: false 
      });
    }
  }
}));

// Helper functions for AsyncStorage
const STORAGE_KEY = '@projects';

const saveProjects = async (projects: Project[]) => {
  try {
    // Convert Date objects to strings for storage
    const projectsToSave = projects.map(project => ({
      ...project,
      startDate: project.startDate ? project.startDate.toISOString() : null,
      endDate: project.endDate ? project.endDate.toISOString() : null,
      createdAt: project.createdAt.toISOString()
    }));
    
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(projectsToSave));
  } catch (error) {
    console.error('Error saving projects:', error);
    throw error;
  }
};

const loadProjects = async (): Promise<Project[]> => {
  try {
    const data = await AsyncStorage.getItem(STORAGE_KEY);
    if (!data) return [];
    
    // Convert string dates back to Date objects
    const projects = JSON.parse(data);
    return projects.map((project: any) => ({
      ...project,
      startDate: project.startDate ? new Date(project.startDate) : null,
      endDate: project.endDate ? new Date(project.endDate) : null,
      createdAt: new Date(project.createdAt)
    }));
  } catch (error) {
    console.error('Error loading projects:', error);
    throw error;
  }
}; 