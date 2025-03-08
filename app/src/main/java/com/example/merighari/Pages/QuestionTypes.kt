package com.example.merighari.Pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.merighari.R
import com.example.merighari.databinding.ActivityQuestionTypesBinding

class QuestionTypes : AppCompatActivity() {

    private lateinit var binding : ActivityQuestionTypesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.freecard.setOnClickListener {
            val intent = Intent(this, GeneralQuestion::class.java)
            startActivity(intent)
        }

        binding.premiumcard.setOnClickListener {
            startActivity(Intent(this@QuestionTypes, PlanActivity::class.java))
        }



    }
}