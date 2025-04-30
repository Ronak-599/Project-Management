import React from 'react';
import { View, StyleSheet, TouchableOpacity } from 'react-native';
import { Searchbar, Text, useTheme } from 'react-native-paper';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

type TaskFilterHeaderProps = {
  searchQuery: string;
  onSearchChange: (query: string) => void;
  onFilterByStatus: () => void;
  onFilterByPriority: () => void;
  onFilterByProject: () => void;
};

const TaskFilterHeader: React.FC<TaskFilterHeaderProps> = ({
  searchQuery,
  onSearchChange,
  onFilterByStatus,
  onFilterByPriority,
  onFilterByProject
}) => {
  const theme = useTheme();

  return (
    <View style={styles.container}>
      <Searchbar
        placeholder="Search tasks..."
        onChangeText={onSearchChange}
        value={searchQuery}
        style={[styles.searchBar, { backgroundColor: theme.colors.surface }]}
        iconColor={theme.colors.primary}
        placeholderTextColor={theme.colors.onSurfaceVariant}
        inputStyle={styles.searchInput}
      />
      
      <View style={styles.filterContainer}>
        <FilterChip 
          label="Status" 
          onPress={onFilterByStatus} 
          icon="filter-variant"
          color="#2196F3"
        />
        
        <FilterChip 
          label="Priority" 
          onPress={onFilterByPriority} 
          icon="trending-up"
          color="#FF9800"
        />
        
        <FilterChip 
          label="Project" 
          onPress={onFilterByProject} 
          icon="folder"
          color="#4CAF50"
        />
      </View>
    </View>
  );
};

type FilterChipProps = {
  label: string;
  onPress: () => void;
  icon: string;
  color: string;
};

const FilterChip: React.FC<FilterChipProps> = ({ label, onPress, icon, color }) => {
  return (
    <TouchableOpacity 
      style={[styles.filterChip, { borderColor: color, backgroundColor: `${color}10` }]} 
      onPress={onPress}
    >
      <Icon name={icon} size={18} color={color} style={styles.filterIcon} />
      <Text style={[styles.filterText, { color }]}>{label}</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: 16,
    paddingTop: 8,
    marginBottom: 16,
  },
  searchBar: {
    elevation: 2,
    borderRadius: 12,
    marginBottom: 16,
    height: 50,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  searchInput: {
    fontSize: 16,
  },
  filterContainer: {
    flexDirection: 'row',
    gap: 12,
    paddingHorizontal: 4,
  },
  filterChip: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 8,
    paddingHorizontal: 14,
    borderWidth: 1,
    borderRadius: 20,
  },
  filterIcon: {
    marginRight: 6,
  },
  filterText: {
    fontWeight: '600',
    fontSize: 13,
  },
});

export default TaskFilterHeader; 