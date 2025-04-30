import React, { createContext, useState, useContext, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { User, createUser } from '../models/User';
import { auth } from '../config/firebase';
import { 
  createUserWithEmailAndPassword, 
  signInWithEmailAndPassword, 
  signOut,
  onAuthStateChanged,
  UserCredential 
} from 'firebase/auth';

interface AuthContextType {
  user: User | null;
  isLoggedIn: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<boolean>;
  register: (username: string, email: string, fullName: string, password: string) => Promise<boolean>;
  logout: () => Promise<boolean>;
}

const AuthContext = createContext<AuthContextType>({
  user: null,
  isLoggedIn: false,
  isLoading: true,
  login: async () => false,
  register: async () => false,
  logout: async () => false,
});

export const useAuth = () => useContext(AuthContext);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Add debug logging for auth state changes
  useEffect(() => {
    console.log('Auth state changed - user:', user ? 'logged in' : 'logged out');
    console.log('isLoggedIn value:', !!user);
  }, [user]);

  useEffect(() => {
    // Check if user is logged in with Firebase
    const unsubscribe = onAuthStateChanged(auth, async (firebaseUser) => {
      if (firebaseUser) {
        try {
          // Check if we have the user's profile data in AsyncStorage
          const userJson = await AsyncStorage.getItem(`@user_${firebaseUser.uid}`);
          
          if (userJson) {
            // If we have cached user data, use it
            const userData = JSON.parse(userJson);
            userData.createdAt = new Date(userData.createdAt);
            setUser(userData);
          } else {
            // If we don't have cached data, create minimal profile
            // In a real app, you would fetch the user profile from a database
            const minimalUser = createUser(
              firebaseUser.uid,
              firebaseUser.email || 'No email',
              firebaseUser.displayName || 'User'
            );
            
            // Save minimal user to storage
            await AsyncStorage.setItem(`@user_${firebaseUser.uid}`, JSON.stringify(minimalUser));
            setUser(minimalUser);
          }
        } catch (error) {
          console.error('Failed to load user from storage', error);
        }
      } else {
        // No user is signed in
        setUser(null);
      }
      setIsLoading(false);
    });

    // Clean up subscription
    return () => unsubscribe();
  }, []);

  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      const userCredential: UserCredential = await signInWithEmailAndPassword(auth, email, password);
      const firebaseUser = userCredential.user;
      
      // Check if we have a user profile stored
      const userJson = await AsyncStorage.getItem(`@user_${firebaseUser.uid}`);
      
      if (userJson) {
        // If we have user data, use it
        const userData = JSON.parse(userJson);
        userData.createdAt = new Date(userData.createdAt);
        setUser(userData);
      } else {
        // If no user data exists, create a new profile
        const newUser = createUser(
          firebaseUser.uid,
          email,
          firebaseUser.displayName || 'User'
        );
        
        // Save user to storage
        await AsyncStorage.setItem(`@user_${firebaseUser.uid}`, JSON.stringify(newUser));
        setUser(newUser);
      }
      
      return true;
    } catch (error) {
      console.error('Login failed', error);
      return false;
    }
  };

  const register = async (
    username: string,
    email: string,
    fullName: string,
    password: string
  ): Promise<boolean> => {
    try {
      // Create user in Firebase Auth
      const userCredential: UserCredential = await createUserWithEmailAndPassword(auth, email, password);
      const firebaseUser = userCredential.user;
      
      // Create a user profile
      const newUser = createUser(username, email, fullName);
      
      // Save user to storage
      await AsyncStorage.setItem(`@user_${firebaseUser.uid}`, JSON.stringify(newUser));
      setUser(newUser);
      
      return true;
    } catch (error) {
      console.error('Registration failed', error);
      return false;
    }
  };

  const logout = async (): Promise<boolean> => {
    try {
      // Sign out from Firebase
      await signOut(auth);
      
      // Clear user data from state
      setUser(null);
      return true;
    } catch (error) {
      console.error('Logout failed with error:', error);
      return false;
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoggedIn: !!user,
        isLoading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}; 