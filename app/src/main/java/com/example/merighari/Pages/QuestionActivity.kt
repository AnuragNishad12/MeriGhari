package com.example.merighari.Pages

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.merighari.Model.Question
import com.example.merighari.R

class QuestionActivity : AppCompatActivity() {
    private lateinit var question: Question
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        question = intent.getSerializableExtra("question") as Question
        position = intent.getIntExtra("position", -1)

        val questionText: TextView = findViewById(R.id.questionText)
        val option1: Button = findViewById(R.id.option1)
        val option2: Button = findViewById(R.id.option2)
        val option3: Button = findViewById(R.id.option3)
        val option4: Button = findViewById(R.id.option4)

        questionText.text = question.questionText
        val options = question.options

        option1.text = options[0]
        option2.text = options[1]
        option3.text = options[2]
        option4.text = options[3]

        val clickListener = View.OnClickListener { view ->
            val selectedAnswer = (view as Button).text.toString()
            if (selectedAnswer == question.correctAnswer) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("position", position)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show()
            }
        }

        option1.setOnClickListener(clickListener)
        option2.setOnClickListener(clickListener)
        option3.setOnClickListener(clickListener)
        option4.setOnClickListener(clickListener)
    }
}
