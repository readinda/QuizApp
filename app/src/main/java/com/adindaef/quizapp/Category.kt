package com.adindaef.quizapp

class Category{
    companion object{
        val MATEMATIKA = 1
        val IPS = 2
        val IPA = 3
    }
    var id: Int = 0
    var nama: String = ""

    constructor(){}

    constructor(nama: String) {
        this.id = id
        this.nama = nama
    }

    override fun toString(): String {
        return nama
    }
}