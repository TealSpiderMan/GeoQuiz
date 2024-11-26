package com.example.geoquiz

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_summary)

        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val score = intent.getIntExtra("score", 0)
        val cheatTokensUsed = intent.getIntExtra("cheatTokensUsed", 0)

        val totalQuestionsTextView: TextView = findViewById(R.id.total_questions_text_view)
        val scoreTextView: TextView = findViewById(R.id.score_text_view)
        val cheatTokensUsedTextView: TextView = findViewById(R.id.cheat_tokens_used_text_view)
        val backButton: Button = findViewById(R.id.back_button)

        totalQuestionsTextView.text = "Total Questions Answered: $totalQuestions"
        scoreTextView.text = "Total Score: $score"
        cheatTokensUsedTextView.text = "Total Cheat Attempts: $cheatTokensUsed"

        backButton.setOnClickListener {
            finish() // Closes the ResultSummaryActivity and returns to the previous activity (MainActivity)
        }
    }
}
