package com.example.llmclientapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llmclientapp.network.OllamaClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var prompt: EditText
    private lateinit var answer: TextView
    private lateinit var progress: ProgressBar

    private val ollama = OllamaClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        // Добавляем отступы от статус-бара и панели навигации
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        prompt = findViewById(R.id.editPrompt)
        answer = findViewById(R.id.textAnswer)
        progress = findViewById(R.id.progress)

        findViewById<Button>(R.id.buttonSend).setOnClickListener {

            val text = prompt.text.toString().trim()

            if (text.isEmpty()) {
                prompt.error = "Введите запрос"
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            answer.text = ""

            CoroutineScope(Dispatchers.IO).launch {

                val result = try {
                    ollama.generate(text)
                } catch (e: Exception) {
                    "Ошибка: ${e.localizedMessage}"
                }

                withContext(Dispatchers.Main) {
                    progress.visibility = View.GONE
                    answer.text = result
                }
            }
        }
    }
}