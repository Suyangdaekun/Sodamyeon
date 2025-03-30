import os
import torch
import cv2
import numpy as np
from PIL import Image, ImageDraw, ImageFont
import mediapipe as mp
import dlib
from face_alignment import FaceAlignment, LandmarksType

# 경고 및 로그 비활성화
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'  # TensorFlow 로그 비활성화
os.environ['ABSL_LOGGING_VERBOSITY'] = '0'  # absl 로그 비활성화
os.environ['KMP_DUPLICATE_LIB_OK'] = 'TRUE'  # OpenMP 경고 무시

# CUDA 메모리 할당 설정 및 디바이스 선택
if torch.cuda.is_available():
    print("CUDA Available:", torch.cuda.is_available())
    print("CUDA Version:", torch.version.cuda)
    
    # 기존 환경 변수 제거
    if 'PYTORCH_CUDA_ALLOC_CONF' in os.environ:
        del os.environ['PYTORCH_CUDA_ALLOC_CONF']
    
    num_gpus = torch.cuda.device_count()
    print(f"Number of GPUs available: {num_gpus}")
    for i in range(num_gpus):
        print(f"GPU {i}: {torch.cuda.get_device_name(i)}")
    
    device = torch.device('cuda')
else:
    print("CUDA is not available. Using CPU.")
    device = torch.device('cpu')

print(f"Using device: {device}")

# 시각화 설정 (색상 변경 및 이름 수정)
colors = {
    'mediapipe': (0, 0, 255),  # 빨강 (BGR)
    'dlib': (255, 0, 0),       # 파랑 (BGR)
    'fan': (0, 255, 0)         # 초록 (BGR)
}

# 이미지 변환 및 그리기 함수
def cv2_to_pil(img):
    """Convert BGR OpenCV image to RGB PIL image"""
    return Image.fromarray(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))

def draw_circle_on_cv2(image, center, radius, color, thickness=-1):
    """Draw circle on OpenCV image"""
    x, y = center
    cv2.circle(image, (int(x), int(y)), radius, color, thickness)

def draw_text_on_cv2(image, text, position, color):
    """Draw text on OpenCV image"""
    font = cv2.FONT_HERSHEY_SIMPLEX
    font_scale = 0.7
    thickness = 2
    cv2.putText(image, text, position, font, font_scale, color, thickness, cv2.LINE_AA)

# 단일 이미지 처리
image_path = r"D:\Coding\AI4FW\image_modeling\sample_images\god.png"  # 이미지 경로를 여기에 입력하세요.
original_image = cv2.imread(image_path)  # BGR 형식으로 로드됨
if original_image is None:
    raise ValueError("Image not found or unable to load.")

rgb_image = cv2.cvtColor(original_image, cv2.COLOR_BGR2RGB)  # MediaPipe용 RGB numpy array

# 1. MediaPipe
mp_face_mesh = mp.solutions.face_mesh.FaceMesh()
results = mp_face_mesh.process(rgb_image)
if results.multi_face_landmarks:
    mediapipe_landmarks = np.array([[lm.x * rgb_image.shape[1], lm.y * rgb_image.shape[0]] 
                            for lm in results.multi_face_landmarks[0].landmark])
    
    # 모든 랜드마크 (468개)를 이미지에 그리기
    for point in mediapipe_landmarks:
        draw_circle_on_cv2(original_image, (point[0], point[1]), 1, colors['mediapipe'])  # 크기를 1로 설정
else:
    print("No face detected by MediaPipe.")

# 2. dlib
detector = dlib.get_frontal_face_detector()
predictor_path = r"D:\Coding\AI4FW\image_modeling\model_comparing\shape_predictor_68_face_landmarks.dat"
predictor = dlib.shape_predictor(predictor_path)
faces = detector(rgb_image)
if len(faces) > 0:
    shape = predictor(rgb_image, faces[0])
    dlib_landmarks = np.array([[shape.part(i).x, shape.part(i).y] for i in range(shape.num_parts)])
    
    # 모든 랜드마크 그리기
    for point in dlib_landmarks:
        draw_circle_on_cv2(original_image, (point[0], point[1]), 1, colors['dlib'])
else:
    print("No face detected by dlib.")

# 3. FAN
fa = FaceAlignment(LandmarksType.TWO_D, flip_input=False, device=device.type)
fan_landmarks = fa.get_landmarks(rgb_image)[0]

# 모든 랜드마크 그리기
for point in fan_landmarks:
    draw_circle_on_cv2(original_image, (point[0], point[1]), 1, colors['fan'])

# 텍스트 추가 (랜드마크 모델 정보)
draw_text_on_cv2(original_image, "MediaPipe", (10, 30), colors['mediapipe'])
draw_text_on_cv2(original_image, "dlib", (10, 60), colors['dlib'])
draw_text_on_cv2(original_image, "FAN", (10, 90), colors['fan'])

# 결과 이미지 출력
cv2.imshow("Landmark Comparison", original_image)
cv2.waitKey(0)  # 아무 키를 누르면 창이 닫힘
cv2.destroyAllWindows()