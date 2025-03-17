package com.example.merighari.Pages.Games

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.merighari.Pages.PlanActivity
import com.example.merighari.R
import com.example.merighari.databinding.ActivityGameTypesBinding

class GameTypes : AppCompatActivity() {

    private lateinit var binding : ActivityGameTypesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


     binding.freecard.setOnClickListener{
         val intent = Intent(this, RabbitGame::class.java)
         startActivity(intent)
     }


        binding.premiumcard.setOnClickListener{
            startActivity(Intent(this,PlanActivity::class.java))
        }






    }
}