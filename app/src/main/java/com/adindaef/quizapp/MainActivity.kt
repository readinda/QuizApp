package com.adindaef.quizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        var REQUEST_CODE_QUIZ = 1

        var SHARED_PREF = "SharePref"
        var KEY_HIGHSCORE = "keyHighscore"

        var highscore: Int = 0
    }

    lateinit var db:QuizDbHelper
    var questionList: ArrayList<Question> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = QuizDbHelper((this))

        loadHighscore()

        btnStartQuiz.setOnClickListener {

            questionList = db.getAllQuestion

            if (questionList.size > 0) {
                val i = Intent(this@MainActivity, QuizActivity::class.java)
                startActivityForResult(i, REQUEST_CODE_QUIZ)
            } else {
                fillQuestion()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_QUIZ){
            if (resultCode == Activity.RESULT_OK){
                val score = data!!.getIntExtra(QuizActivity.EXTRA_SCORE, 0)
                if (score > highscore){
                    updateScore(score)
                }
            }
        }
    }

    private fun updateScore(score: Int) {
        highscore = score
        textHighscore.setText("Highscore: $highscore")

        val prefs = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(KEY_HIGHSCORE, highscore)
        editor.apply()
    }

    private fun loadHighscore() {
        val prefs = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        highscore = prefs.getInt(KEY_HIGHSCORE, 0)
        textHighscore.setText("Highscore: $highscore")

    }

    private fun fillQuestion() {
        val q1 = Question("2 + 2 =", "2", "5", "4", 3, Question.DIFFICULTY_EASY)
        db.addQuestion(q1)
        val q2 = Question("apa ibu kota indonesia?", "Jakarta", "Bali", "Bekasi", 1, Question.DIFFICULTY_MEDIUM)
        db.addQuestion(q2)
        val q3 = Question("Siapa Presiden pertama indonesia", "SBY", "Soekarno", "Jokowi", 2, Question.DIFFICULTY_HARD)
        db.addQuestion(q3)
        val q4 = Question("20 x 76 = ", "1250", "1520", "1025", 2, Question.DIFFICULTY_HARD)
        db.addQuestion(q4)
        val q5 = Question("Bekasi terletak di daerah...", "Jawa Barat", "Jawa Timur", "Jawa Tengah", 1, Question.DIFFICULTY_MEDIUM)
        db.addQuestion(q5)
    }
}
