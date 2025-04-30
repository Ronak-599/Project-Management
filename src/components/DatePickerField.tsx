import React, { useState } from 'react';
import { View, StyleSheet, TouchableOpacity, Platform } from 'react-native';
import { Text, TextInput } from 'react-native-paper';
import DateTimePicker from '@react-native-community/datetimepicker';
import { format } from 'date-fns';

interface DatePickerFieldProps {
  label: string;
  date: Date | null;
  onDateChange: (date: Date | null) => void;
  style?: any;
  minimumDate?: Date | null;
}

const DatePickerField: React.FC<DatePickerFieldProps> = ({
  label,
  date,
  onDateChange,
  style,
  minimumDate
}) => {
  const [showDatePicker, setShowDatePicker] = useState(false);

  const toggleDatePicker = () => {
    setShowDatePicker(!showDatePicker);
  };

  const handleDateChange = (event: any, selectedDate?: Date) => {
    if (Platform.OS === 'android') {
      setShowDatePicker(false);
    }
    
    if (selectedDate) {
      onDateChange(selectedDate);
    }
  };

  const clearDate = () => {
    onDateChange(null);
  };

  return (
    <View style={[styles.container, style]}>
      <TouchableOpacity onPress={toggleDatePicker} activeOpacity={0.7}>
        <View pointerEvents="none">
          <TextInput
            label={label}
            value={date ? format(date, 'MMM dd, yyyy') : ''}
            editable={false}
            mode="outlined"
            outlineStyle={styles.inputOutline}
            style={styles.input}
            right={
              date ? (
                <TextInput.Icon 
                  icon="close-circle"
                  color="#757575" 
                  onPress={clearDate} 
                  forceTextInputFocus={false}
                />
              ) : (
                <TextInput.Icon 
                  icon="calendar" 
                  color="#1976D2"
                  forceTextInputFocus={false}
                />
              )
            }
          />
        </View>
      </TouchableOpacity>

      {showDatePicker && (
        <DateTimePicker
          value={date || new Date()}
          mode="date"
          display={Platform.OS === 'ios' ? 'spinner' : 'default'}
          onChange={handleDateChange}
          minimumDate={minimumDate || undefined}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: '100%',
    marginBottom: 12,
  },
  input: {
    backgroundColor: '#FFFFFF',
  },
  inputOutline: {
    borderRadius: 8,
    borderColor: '#E0E0E0',
  },
});

export default DatePickerField; 