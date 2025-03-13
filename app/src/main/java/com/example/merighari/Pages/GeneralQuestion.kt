package com.example.merighari.Pages

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.merighari.Adapter.QuestionAdapter
import com.example.merighari.Model.Question
import com.example.merighari.R

class GeneralQuestion : AppCompatActivity() {
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: MutableList<Question>
    private lateinit var adBox: ConstraintLayout
    private var dX = 0f
    private var dY = 0f
    private var maxX = 0f
    private var maxY = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_question)
        supportActionBar?.hide()





        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 100 different questions
        questionList = mutableListOf(
            Question("What is the capital of France?", listOf("Paris", "London", "Berlin", "Rome"), "Paris"),
            Question("Who developed Kotlin?", listOf("Google", "JetBrains", "Microsoft", "Oracle"), "JetBrains"),
            Question("Which is a backend framework?", listOf("React", "Spring Boot", "Flutter", "Vue"), "Spring Boot"),
            Question("What is Android's default language?", listOf("Kotlin", "Swift", "C++", "Java"), "Java"),
            Question("What is AI?", listOf("Artificial Intelligence", "Android Interface", "Auto Internet", "Automated Input"), "Artificial Intelligence"),


            Question("Which country has the most natural lakes?", listOf("Canada", "Russia", "Brazil", "United States"), "Canada"),
            Question("Who was the first emperor of China?", listOf("Sun Tzu", "Qin Shi Huang", " Liu Bang", "Confucius"), "Qin Shi Huang"),
            Question("What is the largest volcano in the Solar System?", listOf("Mount Everest", "Mauna Loa", "Olympus Mons", "Mount Etna"), "Olympus Mons"),
            Question("Which element has the highest melting point?", listOf("Carbon", "Tungsten", "Iron", "Platinum"), "Tungsten"),
            Question("What is the square root of 361?", listOf("17", "18", "19", "20"), "19"),


            Question("Which organ in the human body produces insulin?", listOf("Liver", "Pancreas", "Kidney", "Gallbladder"), "Pancreas"),
            Question("Who wrote One Hundred Years of Solitude?", listOf("Gabriel Garcia Marquez", " Paulo Coelho", "Ernest Hemingway", "George Orwell"), "Gabriel Garcia Marquez"),
            Question("What does “HTTP” stand for?",
                listOf("HyperText Transfer Protocol", "Hyperlink Text Transfer Protocol", "High Transfer Text Process", "Hyperlink Transmission Technology"),
                "HyperText Transfer Protocol"),
            Question("What particle has a positive charge?", listOf("Neutron", "Proton", "Electron", "Photon"), "Proton"),
            Question("Who painted The Persistence of Memory?",
                listOf("Pablo Picasso", "Salvador Dalí", "Vincent van Gogh", "Claude Monet"), "Salvador Dalí"),
            Question("In which year did the first episode of Game of Thrones air?",
                listOf("2009", "2010", "2011", "2012"), "2011"),
            Question("Which country has won the most FIFA World Cups?",
                listOf("Germany", "Brazil", "Italy", "Argentina"), "Brazil"),
            Question("What does GDP stand for?",
                listOf("Gross Domestic Product", "General Domestic Price", "Global Development Plan", "Gross Development Profit"), "Gross Domestic Product"),
            Question("Which band released the album The Dark Side of the Moon?",
                listOf("The Beatles", "Led Zeppelin", "Pink Floyd", "The Rolling Stones"), "Pink Floyd"),
            Question("What has keys but can’t open locks?", listOf("A door", "A piano", "A computer", "A clock"), "A piano"),
            Question("Which vitamin is primarily gained from sunlight?",
                listOf("Vitamin A", "Vitamin B", "Vitamin D", "Vitamin K"), "Vitamin D"),

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

        adBox = findViewById(R.id.adBox)
        val closeButton: ImageButton = findViewById(R.id.closeButton)


        adBox.post {
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            maxX = (screenWidth - adBox.width).toFloat()
            maxY = (screenHeight - adBox.height).toFloat()
        }

        adBox.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY


                    view.x = newX.coerceIn(0f, maxX)
                    view.y = newY.coerceIn(0f, maxY)
                }
            }
            true
        }

        closeButton.setOnClickListener {
            adBox.visibility = View.GONE
        }



    }
}