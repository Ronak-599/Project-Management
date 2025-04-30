import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { PaperProvider, MD3LightTheme } from 'react-native-paper';
import { StatusBar } from 'expo-status-bar';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import AppNavigation from './src/navigation/AppNavigation';
import { theme } from './src/theme/theme';
import { AuthProvider } from './src/context/AuthContext';

export default function App() {
  // Create a properly typed theme object
  const appTheme = {
    ...MD3LightTheme,
    ...theme
  };

  return (
    <SafeAreaProvider>
      {/* @ts-ignore */}
      <PaperProvider theme={appTheme}>
        {/* @ts-ignore */}
        <AuthProvider>
          {/* @ts-ignore */}
          <NavigationContainer>
            <StatusBar style="auto" />
            <AppNavigation />
          </NavigationContainer>
        </AuthProvider>
      </PaperProvider>
    </SafeAreaProvider>
  );
} 