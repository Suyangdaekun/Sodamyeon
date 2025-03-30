# Sodamyeon - Senior Dating App with Face Reading & Astrology-Based Matching

<div align="center">
  <img src="app/src/main/res/drawable/logo_sodamyeon.png" alt="Project Logo" style="width:200px; height:auto;"/>
  <br>
  
  [![English](https://img.shields.io/badge/language-English-blue.svg)](README.md) [![한국어](https://img.shields.io/badge/language-한국어-red.svg)](README.ko.md)
</div>

## 📖 Overview
Sodamyeon is a senior dating app that combines traditional wisdom with modern technology.  
The app leverages face reading (physiognomy) and astrology-based matching algorithms to help users find meaningful connections.  
Built using Mediapipe for facial analysis, Flask for backend services, Socket for real-time communication, and Firebase for data management, Sodamyeon provides a seamless and engaging user experience tailored for seniors.

## 🎬 Demo
<div align="center">
  <a href="https://youtube.com/shorts/OM7OZrjQ1wo?feature=share">
    <img src="https://img.youtube.com/vi/OM7OZrjQ1wo/0.jpg" alt="Sodamyeon App Demo" style="width:300px; height:auto;"/>
  </a>
</div>

## ✨ Key Features
Face Reading Analysis:
- Analyzes facial features using Mediapipe to provide personality insights
- Advanced facial landmark detection with Mediapipe's 400+ point model for precise feature tracking
- Model selection based on comprehensive benchmarking of various landmark detection systems
- Physiognomy analysis algorithm based on traditional Eastern face reading principles
- Facial ratio measurement for personality trait prediction
  
Astrology-Based Matching:
- Matches users based on their zodiac signs and astrological compatibility
- Birthdate-based astrology analysis
- Zodiac matching algorithm
- Compatibility analysis system
  
Real-Time Chat:
- Enables instant messaging between matched users via Socket

User Profiles:
- Stores and manages user data securely using Firebase

Senior-Friendly UI:
- Simple and intuitive interface designed for older adults
- Large fonts and clear navigation

## 🧠 Face Landmark Modeling
The facial landmark detection system is a core component of our app's face reading capabilities:

### Technical Implementation
- **Comprehensive benchmarking**: Evaluated Mediapipe, dlib, and FAN to select the best performance model
- **Mediapipe selection**: Chosen for its superior performance with 400+ facial landmarks
- **High-density landmark model**: Identifies detailed facial features including:
  - Jawline contours
  - Eyebrow shapes
  - Nose structure
  - Eye contours
  - Lip shape
- **Feature extraction**: Calculates over 30 facial ratios used in traditional physiognomy
- **Real-time processing**: Optimized for Android mobile devices with 30+ FPS performance

### Physiognomy Analysis
The model extracts key measurements and ratios, including:
- Face shape classification (oval, round, square, heart, etc.)
- Facial thirds and golden ratio measurement
- Eye-to-eye distance ratio
- Nose-to-lip ratio
- Facial symmetry analysis
- Unique feature detection

These measurements are processed through our proprietary algorithm that references traditional Eastern face reading principles, generating a comprehensive personality analysis and compatibility profile.

## 🛠 Tech Stack
| Category | Technologies |
|----------|--------------|
| Backend | Flask, Socket.IO |
| Frontend | Android (Kotlin) | 
| Facial Analysis | Mediapipe, dlib, FAN |
| Real-Time Communication | Socket.IO, Firebase Realtime Database |
| Database & Authentication | Firebase Firestore, Firebase Authentication |
| Notifications | Firebase Cloud Messaging (FCM) |
| Image Processing | OpenCV, NumPy |

## 🚀 Getting Started
### Prerequisites
- Python 3.9+ (for Flask backend)
- Android Studio (for frontend development)
- Firebase account for database and authentication setup
  
### Installation
Clone the repository:
```bash
git clone https://github.com/Suyangdaekun/Sodamyeon.git
```

Backend Server execution:
```bash
cd Sodamyeon/backend
pip install -r requirements.txt
python app.py
```

Configure Firebase:
- Create a Firebase project and download the google-services.json file
- Place it in the app/ directory of the Android project
  
Run the Android app:
- Open the project in Android Studio
- Build and run on an emulator or physical device
  
## 🖥 Usage
Sign Up/Login:
- Users can register using their email or Google account via Firebase Authentication

Profile Creation:
- Upload a profile picture for face reading analysis
- Enter your birth date for astrology-based matching
  
Matching:
- The app analyzes facial features and zodiac compatibility to suggest matches

Chat:
- Once matched, users can communicate in real-time using the chat feature

## 📁 Project Structure
```
.
├── app/                    # Android application
│   ├── src/main/java/com/ryh/suyangdaegun/
│   │   ├── MainActivity.kt        # Main entry point
│   │   ├── ChattingActivity.kt    # Real-time chat implementation
│   │   └── FaceAnalysisActivity.kt # Mediapipe integration
│   └── res/
│       ├── layout/                # XML layouts
│       └── drawable/              # Image assets
│
├── backend/                # Face reading and analysis backend
│   ├── analyze_face.py     # Face analysis logic
│   ├── face_measurements.py # Facial feature measurements
│   ├── report_generator.py # Analysis report generation
│   ├── utils.py            # Utility functions
│   └── app.py              # Entry point for backend server
│
└── README.md               # Project documentation
```

### Notes
- When running on an Android emulator, the backend server is accessed via `10.0.2.2:5001`
- When using on a real device, change the BASE_URL in the `api.kt` file to the actual server address

## 📄 License
MIT License

Licenses of used external libraries:
- Mediapipe: Apache 2.0
- Firebase: Google APIs Terms of Service
- OpenCV: BSD 3-Clause

## 🔒 Security Considerations
- Firebase security rules setup
- User data encryption
- API authentication methods
- GDPR compliance for user data handling

## 🤝 Team
Team Name: Suyang
| Role | Name |
|----------|--------------|
| Project Leader | Dong Hoon Yang |
| Backend Developer | Yang Hwan Ryu |
| Frontend Developer | Ee Hwan Moon |
| UI/UX Designer | Dong Chan Shin |
| Face landmarks modeling | Su Hyok Yun |

## 📬 Contact
For inquiries, please contact:
wintrover@gmail.com

## 🔄 Future Developments
- AI-powered conversation starters
- Enhanced matching algorithms
- Additional traditional matching methods
- Web platform expansion 