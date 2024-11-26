package com.example.geoquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Question(val text: String, val answer: Boolean, var answered: Boolean = false, var timeRemaining: Long = 30000L)

class MainActivity : AppCompatActivity() {

    private val questionBank = listOf(
        Question("Asia is the largest continent in the world.", true),
        Question("The Pacific Ocean is the largest ocean on Earth.", true),
        Question("Mount Everest is the tallest mountain in the world.", true),
        Question("Mount Fuji is located in Hokkaido.", false),
        Question("Russia spans 11 time zones.", true),
        Question("Egypt has more ancient pyramids than Sudan.", false),
        Question("Canada has a larger population than California.", false),
        Question("The state of Wyoming only has 2 escalators.", true),
        Question("The Sahara is the largest desert in the world.", false),
        Question("Afghanistan's national animal is the Snow Leopard.", true),
        Question("Bhutan is the 2nd country to have the TV.", false)

    )

    private var currentIndex = 0
    private var score = 0
    private var answeredQuestions = 0
    private var cheatTokens = 3
    private var timer: CountDownTimer? = null
    private var isCheater = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("currentIndex", 0)
            score = savedInstanceState.getInt("score", 0)
            answeredQuestions = savedInstanceState.getInt("answeredQuestions", 0)
            cheatTokens = savedInstanceState.getInt("cheatTokens", 3)
            isCheater = savedInstanceState.getBoolean("isCheater", false)
            questionBank.forEachIndexed { index, question ->
                question.answered = savedInstanceState.getBoolean("questionAnswered_$index", false)
                question.timeRemaining = savedInstanceState.getLong("timeRemaining_$index", 30000L)
            }
        }

        val cheatButton: Button = findViewById(R.id.cheat_button)
        val trueButton: Button = findViewById(R.id.true_button)
        val falseButton: Button = findViewById(R.id.false_button)
        val previousButton: Button = findViewById(R.id.previous_button)
        val nextButton: Button = findViewById(R.id.next_button)
        val resetButton: Button = findViewById(R.id.reset_button)
        val resultSummaryButton: Button = findViewById(R.id.result_summary_button)

        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }

        cheatButton.setOnClickListener {
            if (cheatTokens > 0) {
                cheatTokens--
                updateCheatTokens()
                highlightCorrectAnswer()
                isCheater = true
            } else {
                Toast.makeText(this, "No cheat tokens left", Toast.LENGTH_SHORT).show()
            }
        }

        nextButton.setOnClickListener {
            moveToNextQuestion()
        }

        previousButton.setOnClickListener {
            moveToPreviousQuestion()
        }

        resetButton.setOnClickListener { resetQuiz() }

        resultSummaryButton.setOnClickListener { showResultSummary() }

        updateQuestion() // Initialize the first question
        updateCheatTokens() // Initialize cheat tokens
        updateResultSummaryButton() // Initialize the state of the result summary button
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentIndex", currentIndex)
        outState.putInt("score", score)
        outState.putInt("answeredQuestions", answeredQuestions)
        outState.putInt("cheatTokens", cheatTokens)
        outState.putBoolean("isCheater", isCheater)
        questionBank.forEachIndexed { index, question ->
            outState.putBoolean("questionAnswered_$index", question.answered)
            outState.putLong("timeRemaining_$index", question.timeRemaining)
        }
    }

    private fun updateQuestion() {
        val questionTextView: TextView = findViewById(R.id.question_text_view)
        val question = questionBank[currentIndex]
        questionTextView.text = question.text

        val trueButton: Button = findViewById(R.id.true_button)
        val falseButton: Button = findViewById(R.id.false_button)

        if (question.answered) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            trueButton.setBackgroundColor(Color.GRAY)
            falseButton.setBackgroundColor(Color.GRAY)
            timer?.cancel()
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            trueButton.setBackgroundColor(Color.parseColor("#4CAF50"))
            falseButton.setBackgroundColor(Color.parseColor("#F44336"))
            startTimer(question.timeRemaining)
        }
    }

    private fun startTimer(timeRemaining: Long) {
        val timerTextView: TextView = findViewById(R.id.timer_text_view)
        timer?.cancel()
        timer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = (millisUntilFinished / 1000).toString()
                questionBank[currentIndex].timeRemaining = millisUntilFinished
            }

            override fun onFinish() {
                timerTextView.text = "0"
                if (!questionBank[currentIndex].answered) {
                    Toast.makeText(this@MainActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                    checkAnswer(false)
                    if (answeredQuestions < questionBank.size) {
                        currentIndex = (currentIndex + 1) % questionBank.size
                        updateQuestion()
                    } else {
                        timer?.cancel()
                        updateResultSummaryButton()
                    }
                }
            }
        }.start()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val question = questionBank[currentIndex]

        if (!question.answered) {
            if (userAnswer == question.answer) {
                score++
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show()
            }

            question.answered = true
            answeredQuestions++

            val trueButton: Button = findViewById(R.id.true_button)
            val falseButton: Button = findViewById(R.id.false_button)
            trueButton.isEnabled = false
            falseButton.isEnabled = false

            trueButton.setBackgroundColor(Color.GRAY)
            falseButton.setBackgroundColor(Color.GRAY)

            timer?.cancel() // Pause the timer when an answer is submitted
        }

        updateResultSummaryButton()

        if (answeredQuestions == questionBank.size) {
            showPercentageScore()
        }
    }

    private fun moveToNextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
        updateQuestion()
    }

    private fun moveToPreviousQuestion() {
        currentIndex = if (currentIndex == 0) questionBank.size - 1 else currentIndex - 1
        updateQuestion()
    }

    private fun highlightCorrectAnswer() {
        val question = questionBank[currentIndex]
        val trueButton: Button = findViewById(R.id.true_button)
        val falseButton: Button = findViewById(R.id.false_button)

        if (question.answer) {
            trueButton.setBackgroundColor(Color.YELLOW)
        } else {
            falseButton.setBackgroundColor(Color.YELLOW)
        }
    }

    private fun updateCheatTokens() {
        val cheatTokensTextView: TextView = findViewById(R.id.cheat_tokens_text_view)
        cheatTokensTextView.text = "Cheat tokens: $cheatTokens"
    }

    private fun updateResultSummaryButton() {
        val resultSummaryButton: Button = findViewById(R.id.result_summary_button)
        if (answeredQuestions == questionBank.size) {
            resultSummaryButton.isEnabled = true
            resultSummaryButton.setBackgroundColor(Color.parseColor("#6200EE")) // Change to an active color
        } else {
            resultSummaryButton.isEnabled = false
            resultSummaryButton.setBackgroundColor(Color.GRAY)
        }
    }

    private fun resetQuiz() {
        currentIndex = 0
        score = 0
        answeredQuestions = 0
        cheatTokens = 3
        isCheater = false

        for (question in questionBank) {
            question.answered = false
            question.timeRemaining = 30000L // Reset the timer for each question
        }

        updateQuestion()
        updateCheatTokens()
        updateResultSummaryButton()
    }

    private fun showPercentageScore() {
        val percentageScore = (score.toDouble() / questionBank.size) * 100
        Toast.makeText(this, "You scored $percentageScore%", Toast.LENGTH_LONG).show()
    }

    private fun showResultSummary() {
        val intent = Intent(this, ResultSummaryActivity::class.java).apply {
            putExtra("totalQuestions", questionBank.size)
            putExtra("score", score)
            putExtra("cheatTokensUsed", 3 - cheatTokens)
        }
        startActivity(intent)
    }
}
