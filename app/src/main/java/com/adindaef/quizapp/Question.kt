package com.adindaef.quizapp

import android.os.Parcel
import android.os.Parcelable

class Question : Parcelable {
    var id:Int = 0
    var question:String? = null
    var option1:String? = null
    var option2:String? = null
    var option3:String? = null
    var answer:Int = 0
    var difficulty: String? = null
    var categoryID:Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        question = parcel.readString()
        option1 = parcel.readString()
        option2 = parcel.readString()
        option3 = parcel.readString()
        answer = parcel.readInt()
        difficulty = parcel.readString()
        categoryID = parcel.readInt()
    }

    constructor(){}

    constructor(
        question: String?,
        option1: String?,
        option2: String?,
        option3: String?,
        answer: Int,
        difficulty: String?,
        categoryId: Int
    ) {
        this.id = id
        this.question = question
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.answer = answer
        this.difficulty = difficulty
        this.categoryID = categoryId
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(question)
        parcel.writeString(option1)
        parcel.writeString(option2)
        parcel.writeString(option3)
        parcel.writeInt(answer)
        parcel.writeString(difficulty)
        parcel.writeInt(categoryID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        val DIFFICULTY_EASY = "Easy"
        val DIFFICULTY_MEDIUM = "Medium"
        val DIFFICULTY_HARD = "Hard"

        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }

        fun getAllDifficultyLevels():Array<String>{
            return arrayOf(DIFFICULTY_EASY, DIFFICULTY_MEDIUM, DIFFICULTY_HARD)
        }
    }
}