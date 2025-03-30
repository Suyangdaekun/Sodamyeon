package com.ryh.suyangdaegun

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ApiService {
    private val client = OkHttpClient()
    // 안드로이드 에뮬레이터에서는 10.0.2.2:5001 사용, 실제 기기에서는 적절한 서버 주소로 변경
    private const val BASE_URL = "http://10.0.2.2:5001" // backend 폴더의 Flask 서버 주소

    data class UserData(
        val birthdate: String = "",
        val email: String = "",
        val gender: String = "",
        val interests: List<String> = emptyList(),
        val nickname: String = "",
        val uid: String = ""
    )

    suspend fun sendUserData(userData: UserData): Result<Unit> = suspendCoroutine { continuation ->
        val json = JSONObject().apply {
            put("birthdate", userData.birthdate)
            put("email", userData.email)
            put("gender", userData.gender)
            put("interests", userData.interests)
            put("nickname", userData.nickname)
            put("uid", userData.uid)
        }

        val request = Request.Builder()
            .url("$BASE_URL/users")
            .post(json.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    continuation.resume(Result.success(Unit))
                } else {
                    continuation.resumeWithException(
                        IOException("서버 에러: ${response.code}")
                    )
                }
                response.close()
            }
        })
    }

    // 얼굴 분석 API 호출
    suspend fun analyzeFace(base64Image: String): Result<String> = suspendCoroutine { continuation ->
        val json = JSONObject().apply {
            put("image", base64Image)
        }

        val request = Request.Builder()
            .url("$BASE_URL/analyze-face")
            .post(json.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: "{}"
                    val jsonResponse = JSONObject(responseBody)
                    val analysis = jsonResponse.optString("analysis", "분석 결과를 가져올 수 없습니다.")
                    continuation.resume(Result.success(analysis))
                } else {
                    continuation.resumeWithException(
                        IOException("서버 에러: ${response.code}")
                    )
                }
                response.close()
            }
        })
    }
}