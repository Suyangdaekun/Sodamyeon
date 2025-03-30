package com.ryh.suyangdaegun

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FaceAnalysisActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaceAnalysisScreen(navController = androidx.navigation.compose.rememberNavController())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceAnalysisScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    // 상태 변수들
    var selectedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var analysisResult by remember { mutableStateOf<String?>(null) }
    var showCamera by remember { mutableStateOf(false) }
    
    // 임시 사진 파일 경로
    val photoFile = remember { 
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = context.getExternalFilesDir("images")
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        } catch (e: IOException) {
            null
        }
    }
    val photoUri = remember(photoFile) {
        photoFile?.let {
            FileProvider.getUriForFile(
                context, 
                "${context.packageName}.fileprovider",
                it
            )
        }
    }
    
    // 카메라 퍼미션 요청
    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showCamera = true
        } else {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }
    
    // 카메라 결과 처리
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(photoUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                selectedImageBitmap = bitmap.asImageBitmap()
                inputStream?.close()
            } catch (e: Exception) {
                Log.e("FaceAnalysis", "Error loading image from camera: ${e.message}")
                Toast.makeText(context, "이미지 로딩 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    // 갤러리 결과 처리
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    selectedImageBitmap = bitmap.asImageBitmap()
                    inputStream?.close()
                } catch (e: Exception) {
                    Log.e("FaceAnalysis", "Error loading image from gallery: ${e.message}")
                    Toast.makeText(context, "이미지 로딩 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    // 분석 함수
    fun analyzeImage() {
        selectedImageBitmap?.let { imageBitmap ->
            isAnalyzing = true
            scope.launch {
                try {
                    // 비트맵을 Base64로 변환
                    val base64Image = convertBitmapToBase64(imageBitmap)
                    
                    // API 호출
                    val result = ApiService.analyzeFace(base64Image)
                    result.onSuccess { analysisText ->
                        analysisResult = analysisText
                    }.onFailure { error ->
                        analysisResult = "분석 오류: ${error.message}"
                    }
                } catch (e: Exception) {
                    analysisResult = "오류 발생: ${e.message}"
                    Log.e("FaceAnalysis", "Error during analysis: ${e.message}")
                } finally {
                    isAnalyzing = false
                }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Text(
                text = "관상 분석",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.width(30.dp))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 이미지 프리뷰
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                .border(2.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageBitmap != null) {
                Image(
                    bitmap = selectedImageBitmap!!,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Text(
                    text = "이미지를 선택하거나\n촬영해주세요",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 버튼 그룹
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 카메라 버튼
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        val cameraPermission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, cameraPermission) 
                            == PackageManager.PERMISSION_GRANTED) {
                            photoUri?.let { takePictureLauncher.launch(it) }
                        } else {
                            requestCameraPermission.launch(cameraPermission)
                        }
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFF5F5F8), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Camera",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Black
                    )
                }
                Text("카메라", fontSize = 14.sp)
            }
            
            // 갤러리 버튼
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageLauncher.launch(intent)
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFF5F5F8), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.gallery),
                        contentDescription = "Gallery",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Black
                    )
                }
                Text("갤러리", fontSize = 14.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 분석 버튼
        Button(
            onClick = { analyzeImage() },
            enabled = selectedImageBitmap != null && !isAnalyzing,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2D3A31)
            )
        ) {
            if (isAnalyzing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "관상 분석하기",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 분석 결과
        if (!analysisResult.isNullOrEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color(0xFFF5F5F8),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "관상 분석 결과",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Divider(color = Color.Gray.copy(alpha = 0.3f))
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = analysisResult ?: "",
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

// 비트맵을 Base64로 변환하는 함수
fun convertBitmapToBase64(bitmap: ImageBitmap): String {
    val androidBitmap = bitmap.asAndroidBitmap()
    val outputStream = ByteArrayOutputStream()
    androidBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    val byteArray = outputStream.toByteArray()
    return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
}
