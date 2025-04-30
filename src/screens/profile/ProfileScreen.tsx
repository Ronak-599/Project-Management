import React from 'react';
import { StyleSheet, View, ScrollView } from 'react-native';
import { Text, Surface, Avatar, Button, Divider, useTheme } from 'react-native-paper';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useAuth } from '../../context/AuthContext';
import { LinearGradient } from 'expo-linear-gradient';

const ProfileScreen = () => {
  const { user, logout } = useAuth();
  const theme = useTheme();

  const handleLogout = async () => {
    await logout();
  };

  if (!user) {
    return (
      <SafeAreaView style={styles.container}>
        <Text>Not logged in</Text>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <LinearGradient
        colors={['#4776E6', '#8E54E9']}
        style={styles.headerGradient}
      />
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <Surface style={styles.profileContainer}>
          <View style={styles.avatarContainer}>
            <Avatar.Icon
              size={100}
              icon="account"
              style={styles.avatar}
              color="#ffffff"
            />
          </View>
          
          <Text variant="headlineMedium" style={styles.name}>
            {user.fullName}
          </Text>
          
          <Text variant="titleMedium" style={styles.username}>
            @{user.username}
          </Text>
          
          <Divider style={styles.divider} />
          
          <View style={styles.infoSection}>
            <View style={styles.infoContainer}>
              <Text variant="labelLarge" style={styles.infoLabel}>
                EMAIL
              </Text>
              <Text variant="bodyLarge" style={styles.infoValue}>
                {user.email}
              </Text>
            </View>
            
            <View style={styles.infoContainer}>
              <Text variant="labelLarge" style={styles.infoLabel}>
                ACCOUNT CREATED
              </Text>
              <Text variant="bodyLarge" style={styles.infoValue}>
                {user.createdAt.toLocaleDateString()}
              </Text>
            </View>
          </View>
          
          <Button
            mode="contained"
            onPress={handleLogout}
            style={styles.logoutButton}
            buttonColor="#f44336"
            textColor="#ffffff"
            icon="logout-variant"
          >
            Sign Out
          </Button>
        </Surface>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f7f7f7',
  },
  headerGradient: {
    position: 'absolute',
    left: 0,
    right: 0,
    top: 0,
    height: 180,
  },
  scrollContent: {
    flexGrow: 1,
    padding: 20,
    paddingTop: 100,
  },
  profileContainer: {
    padding: 30,
    borderRadius: 16,
    elevation: 6,
    alignItems: 'center',
    backgroundColor: '#ffffff',
  },
  avatarContainer: {
    marginBottom: 24,
    borderRadius: 50,
    elevation: 8,
  },
  avatar: {
    backgroundColor: '#8E54E9',
  },
  name: {
    fontWeight: 'bold',
    color: '#212121',
    marginBottom: 6,
    textAlign: 'center',
  },
  username: {
    color: '#757575',
    marginBottom: 24,
  },
  divider: {
    width: '100%',
    height: 1,
    marginBottom: 24,
  },
  infoSection: {
    width: '100%',
  },
  infoContainer: {
    width: '100%',
    marginBottom: 24,
    backgroundColor: '#f9f9f9',
    padding: 16,
    borderRadius: 12,
  },
  infoLabel: {
    color: '#8E54E9',
    marginBottom: 8,
    fontWeight: 'bold',
  },
  infoValue: {
    color: '#212121',
    fontSize: 16,
  },
  logoutButton: {
    marginTop: 16,
    width: '100%',
    borderRadius: 10,
    paddingVertical: 6,
  },
});

export default ProfileScreen; 