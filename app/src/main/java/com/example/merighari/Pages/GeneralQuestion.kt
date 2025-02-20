package com.example.merighari.Pages

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.merighari.Adapter.QuestionAdapter
import com.example.merighari.Model.Question
import com.example.merighari.R

class GeneralQuestion : AppCompatActivity() {
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: MutableList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_question)





        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 100 different questions
        questionList = mutableListOf(
            Question("What is the capital of France?", listOf("Paris", "London", "Berlin", "Rome"), "Paris"),
            Question("Who developed Kotlin?", listOf("Google", "JetBrains", "Microsoft", "Oracle"), "JetBrains"),
            Question("Which is a backend framework?", listOf("React", "Spring Boot", "Flutter", "Vue"), "Spring Boot"),
            Question("What is Android's default language?", listOf("Kotlin", "Swift", "C++", "Java"), "Java"),
            Question("What is AI?", listOf("Artificial Intelligence", "Android Interface", "Auto Internet", "Automated Input"), "Artificial Intelligence"),
            // Add 95 more unique questions
        )

        questionAdapter = QuestionAdapter(questionList) { position ->
            questionList[position].isSolved = true
            questionAdapter.notifyItemChanged(position)
        }
        recyclerView.adapter = questionAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra("position", -1) ?: -1
            if (position != -1) {
                questionAdapter.markQuestionAsSolved(position)
            }
        }
    }
}