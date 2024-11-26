# GeoQuiz

GeoQuiz is a simple Android quiz application that challenges users with geography-based questions. It is built using Kotlin and leverages the ConstraintLayout for its UI design.

## Features

- **Question Navigation**: Users can navigate through questions using the `NEXT` and `PREVIOUS` buttons.
- **Answer Validation**: Users can answer questions by selecting `TRUE` or `FALSE` and receive immediate feedback.
- **Cheat Functionality**: A `CHEAT` button allows users to highlight the correct answer if they have cheat tokens available.
- **Score Tracking**: The app tracks the user’s score based on correct answers.
- **Quiz Reset**: Reset the quiz at any time to start fresh.
- **Result Summary**: View a summary of your performance at the end of the quiz.
- **Cheat Token Management**: Users start with 3 cheat tokens, which decrement with each use.

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/geoquiz.git
   ```

2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical Android device.

## Screenshots

_Add screenshots here to showcase your app’s interface and features._

## Code Highlights

- **Data Class for Questions**:
  ```kotlin
  data class Question(val text: String, val answer: Boolean, var answered: Boolean = false)
  ```
- **Cheat Functionality**:
  ```kotlin
  private fun highlightCorrectAnswer() {
      val question = questionBank[currentIndex]
      val trueButton: Button = findViewById(R.id.true_button)
      val falseButton: Button = findViewById(R.id.false_button)

      if (question.answer) {
          trueButton.setBackgroundColor(Color.parseColor("#81C784")) // Light green
      } else {
          falseButton.setBackgroundColor(Color.parseColor("#FFCDD2")) // Light red
      }
  }
  ```

## How to Use

1. Launch the app.
2. Read the displayed question and select `TRUE` or `FALSE` to answer.
3. Use the `NEXT` and `PREVIOUS` buttons to navigate between questions.
4. Use the `CHEAT` button to highlight the correct answer (if you have tokens available).
5. View your performance with the `RESULT SUMMARY` button.
6. Reset the quiz at any time with the `RESET` button.

## Future Enhancements

- Add more questions.
- Include categories for questions.
- Implement a timer for each question.
- Add visual feedback for correct and incorrect answers.

---

