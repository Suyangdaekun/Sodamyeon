from flask import Flask, request, jsonify
from analyze_face import analyze_face
import os
import base64
import tempfile
from werkzeug.utils import secure_filename
from flask_cors import CORS
import logging
import uuid

app = Flask(__name__)
CORS(app)  # 모든 도메인에서의 요청 허용

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 임시 디렉토리 설정
TEMP_FOLDER = tempfile.gettempdir()
os.makedirs(TEMP_FOLDER, exist_ok=True)

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

@app.route('/analyze-face', methods=['POST'])
def analyze_face_api():
    try:
        if 'image' not in request.json:
            return jsonify({"error": "No image provided"}), 400
        
        # Base64 인코딩된 이미지 받기
        image_data = request.json['image']
        
        # Base64 디코딩
        image_data = image_data.split(',')[1] if ',' in image_data else image_data
        image_bytes = base64.b64decode(image_data)
        
        # 임시 파일로 저장
        file_name = f"{uuid.uuid4()}.jpg"
        file_path = os.path.join(TEMP_FOLDER, secure_filename(file_name))
        
        with open(file_path, 'wb') as f:
            f.write(image_bytes)
        
        logger.info(f"Image saved at {file_path}")
        
        # 얼굴 분석 실행
        result = analyze_face(file_path)
        
        # 임시 파일 삭제
        try:
            os.remove(file_path)
        except:
            pass
        
        return jsonify({
            "success": True,
            "analysis": result
        })
    
    except Exception as e:
        logger.error(f"Error analyzing face: {str(e)}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True) 