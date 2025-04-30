import React, { useState } from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { 
  TextInput, 
  Button, 
  useTheme, 
  Text,
  HelperText,
  Appbar,
  SegmentedButtons,
  RadioButton
} from 'react-native-paper';
import { useNavigation } from '@react-navigation/native';
import { useTaskStore } from '../../store/taskStore';
import { useProjectStore } from '../../store/projectStore';
import { TaskPriority, TaskStatus } from '../../models/Task';
import DatePickerField from '../../components/DatePickerField';

const AddTaskScreen = () => {
  const navigation = useNavigation();
  const theme = useTheme();
  const { addTask } = useTaskStore();
  const { projects } = useProjectStore();
  
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [projectId, setProjectId] = useState('');
  const [status, setStatus] = useState<TaskStatus>(TaskStatus.TODO);
  const [priority, setPriority] = useState<TaskPriority>(TaskPriority.MEDIUM);
  const [dueDate, setDueDate] = useState<Date | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSave = async () => {
    if (!title.trim()) {
      setError('Task title is required');
      return;
    }

    if (!projectId) {
      setError('Please select a project');
      return;
    }

    setLoading(true);
    try {
      await addTask(
        projectId,
        title,
        description,
        status,
        priority,
        dueDate
      );
      navigation.goBack();
    } catch (err) {
      setError('Failed to create task');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <Appbar.Header>
        <Appbar.BackAction onPress={() => navigation.goBack()} />
        <Appbar.Content title="Add New Task" />
        <Appbar.Action icon="check" onPress={handleSave} disabled={loading} />
      </Appbar.Header>

      <ScrollView style={styles.content}>
        {error ? (
          <HelperText type="error" visible={!!error}>
            {error}
          </HelperText>
        ) : null}

        <TextInput
          label="Task Title"
          value={title}
          onChangeText={setTitle}
          mode="outlined"
          style={styles.input}
          error={!title.trim() && error !== ''}
        />

        <TextInput
          label="Description"
          value={description}
          onChangeText={setDescription}
          mode="outlined"
          multiline
          numberOfLines={4}
          style={styles.input}
        />

        <Text style={styles.sectionTitle}>Project</Text>
        <RadioButton.Group 
          onValueChange={value => setProjectId(value)} 
          value={projectId}
        >
          <ScrollView horizontal style={styles.projectList}>
            {projects.map(project => (
              <View key={project.id} style={styles.projectOption}>
                <RadioButton.Item
                  label={project.name}
                  value={project.id}
                  position="leading"
                  style={{ paddingVertical: 4 }}
                />
              </View>
            ))}
          </ScrollView>
        </RadioButton.Group>

        <Text style={styles.sectionTitle}>Priority</Text>
        <SegmentedButtons
          value={priority}
          onValueChange={value => setPriority(value as TaskPriority)}
          buttons={[
            { value: TaskPriority.LOW, label: 'Low' },
            { value: TaskPriority.MEDIUM, label: 'Medium' },
            { value: TaskPriority.HIGH, label: 'High' },
          ]}
          style={styles.segmentedButton}
        />

        <Text style={styles.sectionTitle}>Status</Text>
        <SegmentedButtons
          value={status}
          onValueChange={value => setStatus(value as TaskStatus)}
          buttons={[
            { value: TaskStatus.TODO, label: 'To Do' },
            { value: TaskStatus.IN_PROGRESS, label: 'In Progress' },
            { value: TaskStatus.COMPLETED, label: 'Completed' },
          ]}
          style={styles.segmentedButton}
        />

        <Text style={styles.sectionTitle}>Due Date</Text>
        <DatePickerField
          date={dueDate}
          onDateChange={setDueDate}
          label="Select Due Date"
        />

        <View style={styles.buttonContainer}>
          <Button 
            mode="contained" 
            onPress={handleSave} 
            loading={loading}
            style={styles.button}
          >
            Create Task
          </Button>
          <Button 
            mode="outlined" 
            onPress={() => navigation.goBack()} 
            style={styles.button}
          >
            Cancel
          </Button>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    padding: 16,
  },
  input: {
    marginBottom: 16,
  },
  sectionTitle: {
    marginTop: 8,
    marginBottom: 8,
    fontSize: 16,
    fontWeight: '500',
  },
  segmentedButton: {
    marginBottom: 16,
  },
  projectList: {
    flexDirection: 'row',
    marginBottom: 16,
  },
  projectOption: {
    marginRight: 8,
  },
  buttonContainer: {
    marginTop: 24,
    marginBottom: 32,
  },
  button: {
    marginBottom: 12,
  }
});

export default AddTaskScreen; 