import uuid from 'react-native-uuid';

export enum TaskStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}

export enum TaskPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH'
}

export interface Task {
  id: string;
  projectId: string;
  title: string;
  description: string;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate: Date | null;
  createdAt: Date;
}

export const createTask = (
  projectId: string,
  title: string,
  description: string,
  status: TaskStatus = TaskStatus.TODO,
  priority: TaskPriority = TaskPriority.MEDIUM,
  dueDate: Date | null = null
): Task => ({
  id: uuid.v4().toString(),
  projectId,
  title,
  description,
  status,
  priority,
  dueDate,
  createdAt: new Date()
}); 