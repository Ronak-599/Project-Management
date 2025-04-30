import uuid from 'react-native-uuid';

export interface User {
  id: string;
  username: string;
  email: string;
  fullName: string;
  createdAt: Date;
}

export const createUser = (
  username: string,
  email: string,
  fullName: string
): User => ({
  id: uuid.v4().toString(),
  username,
  email,
  fullName,
  createdAt: new Date()
}); 