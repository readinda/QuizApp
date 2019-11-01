package com.adindaef.quizapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuizDbHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VER) {
    companion object{
        private val DATABASE_NAME = "quiz.db"
        private val DATABASE_VER = 1

        //table
        val TABLE_NAME = "quiz_questions"
        //table column
        val COLUMN_ID = "id"
        val COLUMN_QUESTION = "question"
        val COLUMN_OPTION1 = "option1"
        val COLUMN_OPTION2 = "option2"
        val COLUMN_OPTION3 = "option3"
        val COLUMN_ANSWER = "answer"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY:String = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_QUESTION TEXT," +
                "$COLUMN_OPTION1 TEXT," +
                "$COLUMN_OPTION2 TEXT," +
                "$COLUMN_OPTION3 TEXT," +
                "$COLUMN_ANSWER INTEGER)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addQuestion(question: Question){
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(COLUMN_QUESTION, question.question)
        cv.put(COLUMN_OPTION1, question.option1)
        cv.put(COLUMN_OPTION2, question.option2)
        cv.put(COLUMN_OPTION3, question.option3)
        cv.put(COLUMN_ANSWER, question.answer)
        db.insert(TABLE_NAME,null,cv)
        db.close()
    }

    val getAllQuestion: ArrayList<Question>
    get() {
        val listquestion = ArrayList<Question>()
        val selectquestion = "SELECT * FROM $TABLE_NAME"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectquestion, null)
        if (cursor.moveToFirst()){
            do{
                val question = Question()
                question.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                question.question = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION))
                question.option1 = cursor.getString(cursor.getColumnIndex(COLUMN_OPTION1))
                question.option2 = cursor.getString(cursor.getColumnIndex(COLUMN_OPTION2))
                question.option3 = cursor.getString(cursor.getColumnIndex(COLUMN_OPTION3))
                question.answer = cursor.getInt(cursor.getColumnIndex(COLUMN_ANSWER))

                listquestion.add(question)
            } while (cursor.moveToNext())
        }
        db.close()
        return listquestion
    }


}