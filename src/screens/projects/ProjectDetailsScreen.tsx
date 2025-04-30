import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { useRoute } from '@react-navigation/native';

type ProjectDetailsParams = {
  projectId: string;
};

const ProjectDetailsScreen = () => {
  const route = useRoute();
  const { projectId } = route.params as ProjectDetailsParams;

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Project Details</Text>
      <Text style={styles.subtitle}>Project ID: {projectId}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  subtitle: {
    fontSize: 16,
    color: '#757575',
  },
});

export default ProjectDetailsScreen; 