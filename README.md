#  Project Management System

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
   git clone https://github.com/Ronak-599/Project-Management.git
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
![WhatsApp Image 2025-04-30 at 17 47 25_8e4c967a](https://github.com/user-attachments/assets/5d3ae1a2-d8ca-43d1-a478-8a7a687b9033)
![WhatsApp Image 2025-04-30 at 17 47 24_04388b09](https://github.com/user-attachments/assets/48831151-102b-4687-9ef3-ac1aca0f6a3c)
![WhatsApp Image 2025-04-30 at 17 47 28_f4c24310](https://github.com/user-attachments/assets/96ba8a11-0918-464c-adb0-0deb18034033)
![WhatsApp Image 2025-04-30 at 17 47 27_96bf0525](https://github.com/user-attachments/assets/f6694f39-df38-4b65-9a7c-e983def083c7)
![WhatsApp Image 2025-04-30 at 17 47 27_59acd2e9](https://github.com/user-attachments/assets/ee6d9d82-b671-47b1-af6a-438161ceed82)
![WhatsApp Image 2025-04-30 at 17 47 26_f5aa5231](https://github.com/user-attachments/assets/c1ed1adf-fb8d-417c-806c-76d3ad599629)
![WhatsApp Image 2025-04-30 at 17 47 26_36ff9f06](https://github.com/user-attachments/assets/36f6d8c7-dec5-46cc-ad97-02533fe5c66d)
![WhatsApp Image 2025-04-30 at 17 47 25_fd091df3](https://github.com/user-attachments/assets/f4b6b4a2-9720-48d6-a496-30abd09f2cb2)
![WhatsApp Image 2025-04-30 at 17 47 24_a9e50bd3](https://github.com/user-attachments/assets/84d26b07-c282-4e5d-9a07-a7881eeecf39)
![WhatsApp Image 2025-04-30 at 17 47 23_0bef55f2](https://github.com/user-attachments/assets/dbd88a28-a67d-4d39-a590-dd6a1ff905a7)





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

