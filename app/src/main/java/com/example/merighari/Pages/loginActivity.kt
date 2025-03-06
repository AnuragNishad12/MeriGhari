package com.example.merighari.Pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.merighari.R
import com.example.merighari.databinding.ActivityLoginBinding

class loginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



    }
}