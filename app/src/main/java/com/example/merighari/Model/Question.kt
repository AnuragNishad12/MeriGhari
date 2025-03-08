package com.example.merighari.Model

import java.io.Serializable

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    var isSolved: Boolean = false
) : Serializable
