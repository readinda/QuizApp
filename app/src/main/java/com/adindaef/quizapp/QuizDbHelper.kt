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
        val COLUMN_DIFFICULTY = "difficulty"
        val COLUMN_CATEGORY = "category_id"

        //table category
        val TABLE_CATEGORY = "quiz_category"
        val ID_CATEGORY = "id_category"
        val NAME_CATEGORY = "name_category"

        private var instance: QuizDbHelper? = null

        @Synchronized
        fun getIntance(context: Context): QuizDbHelper{
            if (instance == null){
                instance = QuizDbHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY:String = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_QUESTION TEXT," +
                "$COLUMN_OPTION1 TEXT," +
                "$COLUMN_OPTION2 TEXT," +
                "$COLUMN_OPTION3 TEXT," +
                "$COLUMN_ANSWER INTEGER, " +
                "$COLUMN_DIFFICULTY TEXT," +
                "$COLUMN_CATEGORY INTEGER)")
        db!!.execSQL(CREATE_TABLE_QUERY)

        val CREATE_TABLE_CATEGORY:String = ("CREATE TABLE $TABLE_CATEGORY (" +
                "$ID_CATEGORY INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$NAME_CATEGORY TEXT)")
        db!!.execSQL(CREATE_TABLE_CATEGORY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
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
        cv.put(COLUMN_DIFFICULTY, question.difficulty)
        cv.put(COLUMN_CATEGORY, question.categoryID)
        db.insert(TABLE_NAME,null,cv)
        db.close()
    }

    fun addCategory(category: Category){
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(NAME_CATEGORY, category.nama)
        db.insert(TABLE_CATEGORY, null, cv)
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
                question.difficulty = cursor.getString(cursor.getColumnIndex(COLUMN_DIFFICULTY))
                question.categoryID = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY))

                listquestion.add(question)
            } while (cursor.moveToNext())
        }
        db.close()
        return listquestion
    }

    fun getQuestion(categoryID: Int, difficulty: String): ArrayList<Question> {
        val questionList = ArrayList<Question>()
        val db = this.writableDatabase

//        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DIFFICULTY = ?"
//        val selectArgs = arrayOf(difficulty)
//        val cursor = db.rawQuery(selectQuery, selectArgs)

        val selection = "$COLUMN_CATEGORY = ? AND $COLUMN_DIFFICULTY = ?"
        val selectArgs = arrayOf(categoryID.toString(), difficulty)
        val cursor = db.query(TABLE_NAME, null, selection, selectArgs,
            null, null, null)

        if (cursor.moveToFirst()){
            do{
                val question = Question()
                question.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                question.question = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION))
                question.option1 = cursor.getString(cursor.getColumnIndex(COLUMN_OPTION1))
                question.option2 = cursor.getString(cursor.getColumnIndex(COLUMN_OPTION2))
                question.option3 = cursor.getString(cursor.getColumnIndex(COLUMN_OPTION3))
                question.answer = cursor.getInt(cursor.getColumnIndex(COLUMN_ANSWER))
                question.difficulty = cursor.getString(cursor.getColumnIndex(COLUMN_DIFFICULTY))
                question.categoryID = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY))

                questionList.add(question)
            } while (cursor.moveToNext())
        }
        db.close()
        return questionList
    }

    val getAllCategory: ArrayList<Category>
    get() {
        val listCategory = ArrayList<Category>()
        val selectQueryCategory = "SELECT * FROM $TABLE_CATEGORY"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQueryCategory, null)

        if (cursor.moveToFirst()){
            do {
                val category = Category()
                category.id = cursor.getInt(cursor.getColumnIndex(ID_CATEGORY))
                category.nama = cursor.getString(cursor.getColumnIndex(NAME_CATEGORY))

                listCategory.add(category)
            } while (cursor.moveToNext())
        }
        db.close()
        return listCategory
    }


}