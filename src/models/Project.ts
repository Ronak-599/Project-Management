import uuid from 'react-native-uuid';

export enum ProjectStatus {
  NOT_STARTED = 'NOT_STARTED',
  IN_PROGRESS = 'IN_PROGRESS',
  ON_HOLD = 'ON_HOLD',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export interface Project {
  id: string;
  name: string;
  description: string;
  startDate: Date | null;
  endDate: Date | null;
  status: ProjectStatus;
  createdAt: Date;
}

export const createProject = (
  name: string, 
  description: string, 
  startDate: Date | null = null, 
  endDate: Date | null = null, 
  status: ProjectStatus = ProjectStatus.NOT_STARTED
): Project => ({
  id: uuid.v4().toString(),
  name,
  description,
  startDate,
  endDate,
  status,
  createdAt: new Date()
}); 