import uuid from 'react-native-uuid';

export interface TeamMember {
  id: string;
  name: string;
  email: string;
  role: string;
  createdAt: Date;
}

export const createTeamMember = (
  name: string,
  email: string,
  role: string
): TeamMember => ({
  id: uuid.v4().toString(),
  name,
  email,
  role,
  createdAt: new Date()
}); 