package com.example.llmclientapp.network

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class OllamaClient {

    private val gson = Gson()

    private val client = OkHttpClient()

    private val url = "http://10.0.2.2:11434/api/generate"

    fun generate(prompt: String): String {

        val requestBody = OllamaRequest(
            model = "mistral:7b-instruct-v0.3-q4_K_M",
            prompt = prompt,
            stream = false
        )

        val json = gson.toJson(requestBody)

        val body = json.toRequestBody(
            "application/json".toMediaType()
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->

            if (!response.isSuccessful) {
                return "Ошибка HTTP ${response.code}"
            }

            val text = response.body?.string()
                ?: return "Пустой ответ"

            return gson.fromJson(
                text,
                OllamaResponse::class.java
            ).response
        }
    }
}