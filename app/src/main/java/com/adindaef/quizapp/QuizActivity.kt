package com.adindaef.quizapp

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quiz.*
import java.util.*
import kotlin.collections.ArrayList

class QuizActivity : AppCompatActivity() {
    companion object{
        var EXTRA_SCORE = "extraScore"
        val COUNTDOWN:Long = 30000

        val KEY_SCORE = "keyScore"
        val KEY_QUESTION_COUNT = "keyQuestionCount"
        val KEY_QUESTION_LIST = "keyQuestionList"
        val KEY_TIME_LEFT = "keyTimeLeft"
        val KEY_ANSWERED = "keyAnswered"

    }

    lateinit var db:QuizDbHelper
    var questionList: ArrayList<Question> = ArrayList<Question>()

    //variabel untuk menyimpan warna default dari text
    var textColorDefaultRb: ColorStateList? = null
    var textColorDefaultCd: ColorStateList? = null

    var countDownTimer: CountDownTimer? = null
    var timeLeft: Long = 0

    var questionCounter: Int = 0
    var questionCountTotal: Int = 0
    var currentQuestion: Question? = null

    var score: Int = 0
    var answered: Boolean = false

    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        textColorDefaultCd = textCountdown.textColors
        textColorDefaultRb = rb1.textColors

        val intent = intent
        val difficulty = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY)

        textDifficulty.setText("Difficulty: "+ difficulty)



        if (savedInstanceState == null) {
            db = QuizDbHelper(this)
            questionList = db.getQuestion(difficulty)

            questionCountTotal = questionList.size
            Collections.shuffle(questionList)
            showNextQuestion()
        } else{
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST)!!
            questionCountTotal = questionList.size
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT)
            score = savedInstanceState.getInt(KEY_SCORE)
            timeLeft = savedInstanceState.getLong(KEY_TIME_LEFT)
            answered = savedInstanceState.getBoolean(KEY_ANSWERED)
            currentQuestion = questionList.get(questionCounter - 1)


            if (!answered){
                startCountDown()
            } else{
                updateCountdownText()
                showSolution()
            }
        }


        btnConfirm.setOnClickListener {
            if (timeLeft > 0){
                if (!answered){
                    if (rb1.isChecked || rb2.isChecked || rb3.isChecked){
                        checkAnswer()
                    } else {
                        Toast.makeText(this@QuizActivity, "Please select an answer", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showNextQuestion()
                }
            } else {
                showNextQuestion()
            }
        }
    }

    private fun showNextQuestion() {
        rbGroup.clearCheck()
        rb1.setTextColor(textColorDefaultRb)
        rb2.setTextColor(textColorDefaultRb)
        rb3.setTextColor(textColorDefaultRb)

        if (questionCounter < questionCountTotal){
            currentQuestion = questionList.get(questionCounter)

            textQuestion.setText(currentQuestion!!.question)
            rb1.setText(currentQuestion!!.option1)
            rb2.setText(currentQuestion!!.option2)
            rb3.setText(currentQuestion!!.option3)

            questionCounter++
            textQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal)
            answered = false
            btnConfirm.setText("Confirm")

            timeLeft = COUNTDOWN
            startCountDown()
        } else{
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val result = Intent()
        result.putExtra(EXTRA_SCORE, score)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(timeLeft, 1000){
            override fun onFinish() {
                timeLeft = 0
                updateCountdownText()

                if (rb1.isChecked || rb2.isChecked || rb3.isChecked){
                    checkAnswer()
                } else {
                    showSolution()
                }

            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateCountdownText()
            }
        }.start()
    }

    private fun checkAnswer() {
        answered = true

        countDownTimer!!.cancel()

        var rbSelected:RadioButton = findViewById(rbGroup.checkedRadioButtonId)
        val answer = rbGroup.indexOfChild(rbSelected) + 1

        if (answer == currentQuestion!!.answer){
            score++
            textScore.setText("Score: $score")
        }

        showSolution()
    }

    private fun showSolution() {
        rb1.setTextColor(Color.RED)
        rb2.setTextColor(Color.RED)
        rb3.setTextColor(Color.RED)

        when (currentQuestion!!.answer){
            1 -> {
                rb1.setTextColor(Color.GREEN)
                textQuestion.text = "Answer 1 is Correct"
            }
            2 -> {
                rb2.setTextColor(Color.GREEN)
                textQuestion.text = "Answer 2 is Correct"
            }
            3 -> {
                rb3.setTextColor(Color.GREEN)
                textQuestion.text = "Answer 3 is Correct"
            }

        }



        if (questionCounter < questionCountTotal){
            btnConfirm.text = "Next"
        } else {
            btnConfirm.text = "Finish"
        }
    }

    private fun updateCountdownText() {
        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60

        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        textCountdown.setText(timeFormatted)

        if (timeLeft < 10000){
            textCountdown.setTextColor(Color.RED)
        } else{
            textCountdown.setTextColor(textColorDefaultCd)
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz()
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show()
        }

        backPressedTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null){
            countDownTimer!!.cancel()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SCORE, score)
        outState.putInt(KEY_QUESTION_COUNT, questionCounter)
        outState.putLong(KEY_TIME_LEFT, timeLeft)
        outState.putBoolean(KEY_ANSWERED, answered)
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList)
    }

}
