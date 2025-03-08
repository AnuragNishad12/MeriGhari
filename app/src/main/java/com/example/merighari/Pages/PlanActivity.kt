package com.example.merighari.Pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.merighari.R
import com.example.merighari.databinding.ActivityPlanBinding

class PlanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.cancelbutton.setOnClickListener {
            onBackPressed()
        }






    }
}