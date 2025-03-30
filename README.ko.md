# Sodamyeon - 관상과 궁합을 활용한 시니어 데이팅 앱

<div align="center">
  <img src="app/src/main/res/drawable/logo_sodamyeon.png" alt="프로젝트 로고" style="width:200px; height:auto;"/>
  <br>
  
  [![English](https://img.shields.io/badge/language-English-blue.svg)](README.md) [![한국어](https://img.shields.io/badge/language-한국어-red.svg)](README.ko.md)
</div>

## 📖 개요
소다면은 전통적인 지혜와 현대 기술을 결합한 시니어 데이팅 앱입니다.  
관상(얼굴 읽기)과 별자리 기반 매칭 알고리즘을 활용하여 사용자에게 의미 있는 만남을 주선합니다.  
얼굴 분석을 위한 Mediapipe, 백엔드 서비스를 위한 Flask, 실시간 통신을 위한 Socket, 데이터 관리를 위한 Firebase를 사용하여 시니어에게 맞춤화된 원활하고 매력적인 사용자 경험을 제공합니다.

## 🎬 데모
<div align="center">
  <a href="https://youtube.com/shorts/OM7OZrjQ1wo?feature=share" target="_blank">
    <img src="https://img.youtube.com/vi/OM7OZrjQ1wo/0.jpg" alt="소다면 앱 데모" style="width:300px; height:auto;"/>
  </a>
</div>

## ✨ 주요 기능
얼굴 읽기 분석:
- Mediapipe를 사용하여 얼굴 특징을 분석하고 성격 통찰력 제공
- 정밀한 특징 추적을 위한 Mediapipe의 400개 이상 포인트 모델을 활용한 고급 얼굴 랜드마크 탐지
- 다양한 랜드마크 탐지 시스템의 종합적 벤치마킹 기반 모델 선택
- 전통적인 동양 관상학 원칙 기반 분석 알고리즘
- 성격 특성 예측을 위한 얼굴 비율 측정
  
별자리 기반 매칭:
- 사용자의 별자리와 천문학적 호환성을 기반으로 매칭
- 생년월일 기반 점성술 분석
- 별자리 매칭 알고리즘
- 호환성 분석 시스템
  
실시간 채팅:
- Socket을 통해 매칭된 사용자 간 실시간 메시징 지원

사용자 프로필:
- Firebase를 사용하여 사용자 데이터를 안전하게 저장 및 관리

시니어 친화적 UI:
- 노년층을 위해 설계된 간단하고 직관적인 인터페이스
- 큰 글꼴과 명확한 탐색

## 🧠 얼굴 랜드마크 모델링
얼굴 랜드마크 탐지 시스템은 앱의 관상 읽기 기능의 핵심 구성 요소입니다:

### 기술적 구현
- **포괄적 벤치마킹**: 최고 성능 모델 선택을 위해 Mediapipe, dlib, FAN 평가
- **Mediapipe 선택**: 400개 이상의 얼굴 랜드마크로 우수한 성능을 제공하여 선택
- **고밀도 랜드마크 모델**: 다음을 포함한 세부적인 얼굴 특징 식별:
  - 턱선 윤곽
  - 눈썹 모양
  - 코 구조
  - 눈 윤곽
  - 입술 모양
- **특징 추출**: 전통적인 관상학에서 사용되는 30개 이상의 얼굴 비율 계산
- **실시간 처리**: 안드로이드 모바일 기기에서 30+ FPS 성능으로 최적화

### 관상학 분석
모델은 다음을 포함한 주요 측정값과 비율을 추출합니다:
- 얼굴 모양 분류 (타원형, 둥근형, 사각형, 하트형 등)
- 얼굴 삼등분 및 황금 비율 측정
- 눈과 눈 사이 거리 비율
- 코-입술 비율
- 얼굴 대칭 분석
- 독특한 특징 감지

이러한 측정값은 전통적인 동양 관상학 원칙을 참조하는 독자적인 알고리즘을 통해 처리되어 포괄적인 성격 분석 및 호환성 프로필을 생성합니다.

## 🛠 기술 스택
| 분류 | 기술 |
|----------|--------------|
| 백엔드 | Flask, Socket.IO |
| 프론트엔드 | Android (Kotlin) | 
| 얼굴 분석 | Mediapipe, dlib, FAN |
| 실시간 통신 | Socket.IO, Firebase Realtime Database |
| 데이터베이스 및 인증 | Firebase Firestore, Firebase Authentication |
| 알림 | Firebase Cloud Messaging (FCM) |
| 이미지 처리 | OpenCV, NumPy |
| 컨테이너화 | Docker, Docker Compose |

## 🚀 시작하기
### 사전 요구 사항
- Python 3.9+ (Flask 백엔드용)
- Android Studio (프론트엔드 개발용)
- 데이터베이스 및 인증 설정을 위한 Firebase 계정
- Docker 및 Docker Compose (선택사항, 컨테이너화된 배포용)
  
### 설치
저장소 복제:
```bash
git clone https://github.com/Suyangdaekun/Sodamyeon.git
```

#### 옵션 1: 전통적인 설정

백엔드 서버 실행:
```bash
cd Sodamyeon/backend
pip install -r requirements.txt
python app.py
```

Firebase 구성:
- Firebase 프로젝트를 생성하고 google-services.json 파일 다운로드
- 안드로이드 프로젝트의 app/ 디렉토리에 배치
  
안드로이드 앱 실행:
- Android Studio에서 프로젝트 열기
- 에뮬레이터 또는 실제 기기에서 빌드 및 실행

#### 옵션 2: Docker 설정

Docker를 사용하여 백엔드 실행:
```bash
# 프로젝트 루트 디렉토리에서
docker compose up -d
```

또는 백엔드만 빌드하고 실행:
```bash
# 백엔드 디렉토리에서
cd Sodamyeon/backend
docker build -t sodamyeon-backend .
docker run -p 5001:5001 sodamyeon-backend
```

전통적인 설정과 마찬가지로 Firebase를 구성하고 안드로이드 앱을 실행합니다.
  
## 🖥 사용법
회원가입/로그인:
- 사용자는 Firebase Authentication을 통해 이메일 또는 Google 계정을 사용하여 등록 가능

프로필 생성:
- 관상 분석을 위한 프로필 사진 업로드
- 별자리 기반 매칭을 위한 생년월일 입력
  
매칭:
- 앱이 얼굴 특징과 별자리 호환성을 분석하여 매칭 제안

채팅:
- 매칭되면 사용자는 채팅 기능을 사용하여 실시간으로 대화 가능

## 📁 프로젝트 구조
```
.
├── app/                    # 안드로이드 애플리케이션
│   ├── src/main/java/com/ryh/suyangdaegun/
│   │   ├── MainActivity.kt        # 메인 진입점
│   │   ├── ChattingActivity.kt    # 실시간 채팅 구현
│   │   └── FaceAnalysisActivity.kt # Mediapipe 통합
│   └── res/
│       ├── layout/                # XML 레이아웃
│       └── drawable/              # 이미지 에셋
│
├── backend/                # 관상 읽기 및 분석 백엔드
│   ├── analyze_face.py     # 얼굴 분석 로직
│   ├── face_measurements.py # 얼굴 특징 측정
│   ├── report_generator.py # 분석 보고서 생성
│   ├── utils.py            # 유틸리티 함수
│   ├── app.py              # 백엔드 서버 진입점
│   ├── Dockerfile          # 백엔드용 Dockerfile
│   └── requirements.txt    # Python 의존성
│
├── Dockerfile              # 메인 Dockerfile
├── docker-compose.yml      # Docker Compose 구성
└── README.md               # 프로젝트 문서
```

### 참고 사항
- 안드로이드 에뮬레이터에서 실행 시 백엔드 서버는 `10.0.2.2:5001`을 통해 접근
- 실제 기기에서 사용 시 `api.kt` 파일의 BASE_URL을 실제 서버 주소로 변경
- Docker 사용 시 백엔드는 `localhost:5001`에서 접근 가능

## 📄 라이센스
MIT License

사용된 외부 라이브러리의 라이센스:
- Mediapipe: Apache 2.0
- Firebase: Google APIs 이용 약관
- OpenCV: BSD 3-Clause

## 🔒 보안 고려 사항
- Firebase 보안 규칙 설정
- 사용자 데이터 암호화
- API 인증 방법
- 사용자 데이터 처리를 위한 GDPR 준수

## 🤝 팀
팀 이름: 수양
| 역할 | 이름 |
|----------|--------------|
| 프로젝트 리더 | 양동훈 |
| 백엔드 개발자 | 류양환 |
| 프론트엔드 개발자 | 문이환 |
| UI/UX 디자이너 | 신동찬 |
| 얼굴 랜드마크 모델링 | 윤수혁 |

## 📬 연락처
문의 사항은 다음 주소로 연락주세요:
wintrover@gmail.com

## 🔄 향후 개발 계획
- AI 기반 대화 시작 제안
- 향상된 매칭 알고리즘
- 추가적인 전통 매칭 방법
- 웹 플랫폼 확장