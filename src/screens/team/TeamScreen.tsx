import React, { useState } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity, TextInput, StatusBar } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { IconButton, Avatar } from 'react-native-paper';

const mockTeamMembers = [];

const TeamScreen = () => {
  const [searchQuery, setSearchQuery] = useState('');

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor="#2196F3" />
      
      {/* Header */}
      <View style={styles.header}>
        <View style={styles.logoContainer}>
          <IconButton 
            icon="cube-outline" 
            iconColor="#fff" 
            size={26} 
            style={styles.logoIcon} 
          />
          <Text style={styles.logoText}>TaskFlow</Text>
        </View>
      </View>
      
      {/* Search Bar */}
      <View style={styles.searchContainer}>
        <IconButton icon="magnify" size={24} iconColor="#666" style={styles.searchIcon} />
        <TextInput
          style={styles.searchInput}
          placeholder="Search team members..."
          placeholderTextColor="#666"
          value={searchQuery}
          onChangeText={setSearchQuery}
        />
      </View>
      
      {/* Team Member Count */}
      <Text style={styles.memberCount}>{mockTeamMembers.length} team member(s)</Text>
      
      {/* Team Member List */}
      <FlatList
        data={mockTeamMembers}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.memberCard}>
            <View style={styles.memberInfo}>
              <View style={styles.avatarContainer}>
                <Avatar.Icon 
                  size={50} 
                  icon="account" 
                  style={[styles.avatar, { backgroundColor: "#E3F2FD" }]} 
                  color="#2196F3" 
                />
              </View>
              <View style={styles.textContainer}>
                <Text style={styles.memberName}>{item.name}</Text>
                <Text style={styles.memberEmail}>{item.email}</Text>
                <View style={styles.roleContainer}>
                  <Text style={styles.memberRole}>{item.role}</Text>
                </View>
              </View>
            </View>
            <TouchableOpacity style={styles.deleteButton}>
              <IconButton icon="delete" iconColor="#F44336" size={20} />
            </TouchableOpacity>
          </View>
        )}
        contentContainerStyle={styles.list}
      />
      
      {/* Floating Action Button */}
      <TouchableOpacity style={styles.fab}>
        <IconButton icon="plus" iconColor="#fff" size={24} />
      </TouchableOpacity>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f0f0f0',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 12,
    backgroundColor: '#2196F3',
  },
  logoContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  logoIcon: {
    margin: 0,
  },
  logoText: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
  },
  searchContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#fff',
    marginHorizontal: 16,
    marginVertical: 16,
    borderRadius: 8,
    elevation: 2,
  },
  searchIcon: {
    margin: 0,
  },
  searchInput: {
    flex: 1,
    height: 50,
    fontSize: 16,
  },
  memberCount: {
    fontSize: 16,
    marginHorizontal: 16,
    marginBottom: 12,
    color: '#424242',
  },
  list: {
    paddingHorizontal: 16,
  },
  memberCard: {
    backgroundColor: '#fff',
    borderRadius: 8,
    marginBottom: 16,
    padding: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    elevation: 2,
  },
  memberInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  avatarContainer: {
    marginRight: 16,
  },
  avatar: {
    borderRadius: 25,
  },
  textContainer: {
    flex: 1,
  },
  memberName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#212121',
  },
  memberEmail: {
    fontSize: 14,
    color: '#757575',
    marginBottom: 8,
  },
  roleContainer: {
    backgroundColor: '#E0F7FA',
    alignSelf: 'flex-start',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  memberRole: {
    fontSize: 14,
    color: '#00ACC1',
  },
  deleteButton: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  fab: {
    position: 'absolute',
    right: 16,
    bottom: 16,
    backgroundColor: '#2196F3',
    width: 56,
    height: 56,
    borderRadius: 28,
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 6,
  },
});

export default TeamScreen; 