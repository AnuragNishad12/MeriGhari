package com.example.merighari.Pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.merighari.R
import com.example.merighari.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        binding.cardSignUp.setOnClickListener {
            if (validateInputs()) {
                Toast.makeText(this, "All fields are filled!", Toast.LENGTH_SHORT).show()
            }
        }



    }






    fun validateInputs(): Boolean {
        var isValid = true

        if (binding.nameSignup.text.toString().trim().isEmpty()) {
            binding.nameSignup.error = "Please fill out this field"
            isValid = false
        }

        if (binding.emailSignUp.text.toString().trim().isEmpty()) {
            binding.emailSignUp.error = "Please fill out this field"
            isValid = false
        }

        if (binding.PasswordSignUp.text.toString().trim().isEmpty()) {
            binding.PasswordSignUp.error = "Please fill out this field"
            isValid = false
        }

        return isValid
    }



}