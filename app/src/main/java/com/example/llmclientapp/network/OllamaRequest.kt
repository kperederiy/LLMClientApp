package com.example.llmclientapp.network

data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false
)