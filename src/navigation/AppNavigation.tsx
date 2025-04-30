import React, { useEffect } from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useAuth } from '../context/AuthContext';
import { RootStackParamList } from './types';
import { Icon } from 'react-native-paper';

// Screens
import LoginScreen from '../screens/auth/LoginScreen';
import RegisterScreen from '../screens/auth/RegisterScreen';
import DashboardScreen from '../screens/dashboard/DashboardScreen';
import ProjectsScreen from '../screens/projects/ProjectsScreen';
import ProjectDetailsScreen from '../screens/projects/ProjectDetailsScreen';
import TasksScreen from '../screens/tasks/TasksScreen';
import AddTaskScreen from '../screens/tasks/AddTaskScreen';
import TeamScreen from '../screens/team/TeamScreen';
import ProfileScreen from '../screens/profile/ProfileScreen';

const Stack = createNativeStackNavigator<RootStackParamList>();
const Tab = createBottomTabNavigator<RootStackParamList>();

const TabNavigation = () => {
  return (
    <Tab.Navigator
      screenOptions={{
        tabBarActiveTintColor: '#2196F3',
        tabBarInactiveTintColor: '#757575',
        tabBarLabelStyle: { fontSize: 12 },
        tabBarStyle: { paddingVertical: 5 },
        headerShown: false,
      }}
    >
      <Tab.Screen 
        name="Dashboard" 
        component={DashboardScreen} 
        options={{
          tabBarIcon: ({ color, size }) => (
            <Icon source="view-dashboard" size={size} color={color} />
          ),
        }}
      />
      <Tab.Screen 
        name="Projects" 
        component={ProjectsScreen} 
        options={{
          tabBarIcon: ({ color, size }) => (
            <Icon source="folder-outline" size={size} color={color} />
          ),
        }}
      />
      <Tab.Screen 
        name="Tasks" 
        component={TasksScreen} 
        options={{
          tabBarIcon: ({ color, size }) => (
            <Icon source="format-list-bulleted" size={size} color={color} />
          ),
        }}
      />
      <Tab.Screen 
        name="Team" 
        component={TeamScreen} 
        options={{
          tabBarIcon: ({ color, size }) => (
            <Icon source="account-group-outline" size={size} color={color} />
          ),
        }}
      />
      <Tab.Screen 
        name="Profile" 
        component={ProfileScreen} 
        options={{
          tabBarIcon: ({ color, size }) => (
            <Icon source="account" size={size} color={color} />
          ),
        }}
      />
    </Tab.Navigator>
  );
};

const AppNavigation = () => {
  const { isLoggedIn, isLoading } = useAuth();

  // Add debugging for authentication state changes
  useEffect(() => {
    console.log('Auth state changed - isLoggedIn:', isLoggedIn);
  }, [isLoggedIn]);

  if (isLoading) {
    // You could show a splash screen here
    console.log('Auth state loading...');
    return null;
  }

  return (
    <Stack.Navigator
      screenOptions={{
        headerShown: false,
      }}
    >
      {!isLoggedIn ? (
        // Auth screens
        <>
          <Stack.Screen name="Login" component={LoginScreen} />
          <Stack.Screen name="Register" component={RegisterScreen} />
        </>
      ) : (
        // App screens
        <>
          <Stack.Screen name="Dashboard" component={TabNavigation} />
          <Stack.Screen
            name="ProjectDetails"
            component={ProjectDetailsScreen}
            options={{
              headerShown: true,
              title: 'Project Details',
              headerBackTitleVisible: false,
            }}
          />
          <Stack.Screen
            name="AddTask"
            component={AddTaskScreen}
            options={{
              headerShown: false,
              presentation: 'modal',
            }}
          />
        </>
      )}
    </Stack.Navigator>
  );
};

export default AppNavigation; 