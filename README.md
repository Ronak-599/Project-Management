# TaskFlow - Project Management System

A comprehensive project management mobile application built with React Native and Expo, designed to help teams organize projects, manage tasks, and track progress efficiently.

## ✨ Features

- **User Authentication** - Secure login and registration system
- **Project Management** - Create, view, edit, and delete projects
- **Task Management** - Create, assign, prioritize, and mark tasks as complete
- **Team Collaboration** - Invite and manage team members
- **Dashboard** - Visual statistics and progress tracking
- **Modern UI/UX** - Clean, intuitive interface with responsive design

## 🛠️ Technologies Used

- **Frontend**: React Native, TypeScript, Expo
- **UI Components**: React Native Paper
- **Navigation**: React Navigation
- **State Management**: Zustand
- **Backend Services**: Firebase
- **Local Storage**: AsyncStorage
- **Date Handling**: date-fns
- **Icons**: React Native Vector Icons

## 📋 Prerequisites

- Node.js (v14 or newer)
- npm or yarn
- Expo CLI (`npm install -g expo-cli`)
- Firebase account (for backend services)

## 🚀 Setup and Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/taskflow.git
   cd taskflow
   ```

2. **Install dependencies**:
   ```bash
   npm install
   # or
   yarn install
   ```

3. **Configure Firebase**:
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Set up Authentication, Firestore, and Storage
   - Add your Firebase configuration to the project (see FIREBASE_SETUP.md)

4. **Start the development server**:
   ```bash
   npm start
   # or
   yarn start
   ```

5. **Run on device or emulator**:
   - iOS: Press `i` in the terminal or use `npm run ios`
   - Android: Press `a` in the terminal or use `npm run android`
   - Web: Press `w` in the terminal or use `npm run web`

## 📁 Project Structure

```
src/
├── components/     # Reusable UI components
├── context/        # React Context providers
├── models/         # TypeScript interfaces and models
├── navigation/     # Navigation configuration
├── screens/        # Application screens
├── store/          # State management with Zustand
├── theme/          # Theme and styling
└── utils/          # Utility functions
```



## 📷 Screenshots

*Screenshots will be added soon*

## 🔧 Available Scripts

- `npm start` - Start the Expo development server
- `npm run android` - Run on Android device/emulator
- `npm run ios` - Run on iOS simulator
- `npm run web` - Run in web browser
- `npm test` - Run tests

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

