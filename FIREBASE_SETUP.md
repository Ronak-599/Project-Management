# Firebase Authentication Setup

This document will guide you through setting up your Firebase project and integrating it with the app.

## 1. Create a Firebase Project

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" and follow the steps to create a new project
3. Give your project a name like "TaskFlow" or "PMS App"
4. Follow the prompts to set up Google Analytics (optional)
5. Click "Create project"

## 2. Set up Firebase Authentication

1. In your Firebase project, navigate to "Authentication" in the left sidebar
2. Click "Get started"
3. Enable the "Email/Password" sign-in method by clicking on it and toggling the switch
4. Click "Save"

## 3. Register Your App with Firebase

1. From the Firebase project overview page, click the web icon (</>) to add a web app
2. Give your app a nickname (e.g., "TaskFlow Web")
3. Check the option for "Also set up Firebase Hosting" if you plan to deploy the app
4. Click "Register app"
5. Firebase will show you the configuration object. It will look like this:

```javascript
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
  appId: "YOUR_APP_ID"
};
```

## 4. Update Your App's Firebase Configuration

1. Open the file `src/config/firebase.ts` in your project
2. Replace the placeholder values in the `firebaseConfig` object with your actual Firebase configuration values

## 5. Testing

1. Run your app with `npm start`
2. Test the sign-up functionality by creating a new account
3. Test the sign-in functionality with your newly created account
4. Verify that you can view your profile information after signing in
5. Test the sign-out functionality

## 6. Additional Firebase Features (Optional)

Once you have authentication working, you might want to add other Firebase features:

- **Firestore Database**: For storing user data, projects, and tasks
- **Cloud Functions**: For server-side code
- **Storage**: For storing files and images
- **Hosting**: For deploying your web app

## 7. Security Rules

Don't forget to set up security rules for your Firebase services to ensure that only authenticated users can access their own data.

## Troubleshooting

- If you encounter any errors during sign-up or sign-in, check the browser console for Firebase-specific error messages
- Verify that your Firebase configuration values are correctly copied from the Firebase console
- Ensure that Email/Password authentication is enabled in your Firebase project 