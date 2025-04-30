import { Task, TaskStatus, TaskPriority } from '../models/Task';
import { addDays } from 'date-fns';

export const SAMPLE_TASKS: Task[] = [];

export const SAMPLE_PROJECTS = [
  {
    id: 'mobile-app',
    name: 'Mobile App Development',
    color: '#2196F3'
  },
  {
    id: 'backend',
    name: 'Backend Services',
    color: '#4CAF50'
  },
  {
    id: 'test',
    name: 'Test Project',
    color: '#9C27B0'
  }
]; 