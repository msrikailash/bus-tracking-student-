# Student Bus Tracking Application

A comprehensive Android application for tracking school buses in real-time, built with Java and following Material Design 3 principles.

## Features

### 🔐 Authentication & Profile Management
- Student login with Student ID and password
- Biometric authentication support (fingerprint/face)
- Profile management with student information
- Password reset functionality
- Multi-device login support

### 🚌 Real-Time Bus Tracking
- Live bus location on interactive Google Maps
- Real-time location updates every 10 seconds
- Bus route visualization with all stops
- ETA calculations to next stop
- Bus speed and direction indicators
- Traffic condition awareness

### 📅 Schedule Management
- Daily and weekly bus schedules
- Pickup and drop-off times
- Schedule change notifications
- Holiday and special event schedules
- Today's schedule highlighting

### ✅ Check-in/Check-out System
- GPS-based location verification
- QR code scanning at bus stops
- Manual check-in option
- Automatic attendance recording
- Location verification within 100 meters

### 🔔 Notification System
- Push notifications for bus arrivals
- Delay and cancellation alerts
- Emergency notifications
- Schedule change updates
- Customizable notification preferences

### 📊 Trip History & Analytics
- Complete trip history
- Attendance records
- Trip duration tracking
- Performance analytics
- Data export functionality

### ⚙️ Settings & Preferences
- Notification settings
- Location tracking preferences
- Biometric authentication toggle
- Theme selection (Light/Dark/System)
- Privacy settings

## Technical Architecture

### 🏗️ Architecture Pattern
- **MVVM (Model-View-ViewModel)** pattern
- **Repository pattern** for data management
- **Dependency Injection** for loose coupling

### 🛠️ Technologies Used
- **Language**: Java
- **UI Framework**: Material Design 3
- **Backend**: Firebase (Authentication, Firestore, Cloud Messaging)
- **Maps**: Google Maps API
- **Location**: Google Play Services Location
- **QR Scanning**: ZXing library
- **Networking**: Retrofit 2
- **Image Loading**: Glide
- **Date/Time**: ThreeTenABP

### 📱 Key Components

#### Activities
- `MainActivity` - Main navigation hub with bottom navigation
- `LoginActivity` - Student authentication
- `RegisterActivity` - New student registration
- `MapsActivity` - Real-time bus tracking
- `ScheduleActivity` - Bus schedule management
- `CheckInActivity` - Check-in/check-out functionality
- `SettingsActivity` - App preferences
- `QRScannerActivity` - QR code scanning

#### Services
- `AuthService` - Authentication and user management
- `LocationService` - Background location tracking
- `MyFirebaseMessagingService` - Push notification handling

#### Models
- `Student` - Student profile data
- `Bus` - Bus information and location
- `Route` - Bus route and stops
- `Trip` - Trip records and attendance
- `Notification` - Push notification data
- `Schedule` - Bus schedule information

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0+)
- Google Play Services
- Firebase project setup

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd student-bus-tracker
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project folder

3. **Configure Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
   - Add Android app with package name: `com.example.student`
   - Download `google-services.json` and replace the existing one in `app/` directory
   - Enable Authentication, Firestore, and Cloud Messaging

4. **Configure Google Maps**
   - Get Google Maps API key from [Google Cloud Console](https://console.cloud.google.com)
   - Replace `YOUR_GOOGLE_MAPS_API_KEY` in `app/src/main/res/values/strings.xml`

5. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## Configuration

### Firebase Setup
1. Enable Authentication with Email/Password
2. Create Firestore database
3. Set up Cloud Messaging
4. Configure security rules

### Google Maps Setup
1. Enable Maps SDK for Android
2. Enable Places API
3. Enable Directions API
4. Restrict API key to your app

### Permissions
The app requires the following permissions:
- `ACCESS_FINE_LOCATION` - For GPS tracking
- `ACCESS_COARSE_LOCATION` - For approximate location
- `CAMERA` - For QR code scanning
- `INTERNET` - For network communication
- `VIBRATE` - For notifications
- `WAKE_LOCK` - For background services

## Usage

### For Students
1. **Registration**: Create account with Student ID, name, email, and grade
2. **Login**: Use Student ID and password or biometric authentication
3. **Track Bus**: View real-time bus location on map
4. **Check Schedule**: View daily and weekly pickup/drop-off times
5. **Check-in**: Use GPS or QR code to check in/out at bus stops
6. **Notifications**: Receive alerts for delays, arrivals, and emergencies

### For Administrators
- Monitor student attendance
- Track bus locations
- Send notifications
- Manage schedules
- View analytics

## Security Features

- **Data Encryption**: All data encrypted in transit and at rest
- **Authentication**: Secure Firebase Authentication
- **Privacy**: COPPA compliance for students under 13
- **Location Privacy**: Location data only used for check-in verification
- **Session Management**: Automatic logout for security

## Performance

- **App Launch**: < 3 seconds
- **Map Loading**: < 5 seconds
- **Location Updates**: Every 10 seconds
- **Memory Usage**: < 200MB
- **Battery Consumption**: < 5% per hour

## Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## Deployment

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## Roadmap

### Future Features
- [ ] Parent/Guardian app integration
- [ ] Driver app integration
- [ ] Advanced analytics dashboard
- [ ] Multi-language support
- [ ] Offline mode improvements
- [ ] Voice navigation
- [ ] Weather integration
- [ ] Emergency SOS feature

## Changelog

### Version 1.0.0 (December 2024)
- Initial release
- Complete authentication system
- Real-time bus tracking
- Check-in/check-out functionality
- Schedule management
- Push notifications
- Trip history
- Settings and preferences

---

**Built with ❤️ for student safety and convenience**

