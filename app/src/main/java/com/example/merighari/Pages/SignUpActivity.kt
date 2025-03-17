package com.example.merighari.Pages


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.merighari.Model.UserData
import com.example.merighari.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("AlarmUsers")


        if (auth.currentUser != null) {
            moveToLandingPage()
        }


        binding.cardSignUp.setOnClickListener {
            checkUserExistsAndRegister()
        }
    }

    private fun checkUserExistsAndRegister() {
        val name = binding.nameSignup.text.toString().trim()
        val email = binding.emailSignUp.text.toString().trim()
        val password = binding.PasswordSignUp.text.toString().trim()

        if (!validateInputs()) return


        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (!signInMethods.isNullOrEmpty()) {

                        Toast.makeText(this, "User already registered!", Toast.LENGTH_SHORT).show()
                        moveToLandingPage()
                    } else {

                        registerUser(name, email, password)
                    }
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val user = UserData(name, email, password)

                    databaseRef.child(userId).setValue(user).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            Toast.makeText(this, "User Registered Successfully!", Toast.LENGTH_SHORT).show()
                            moveToLandingPage()
                        } else {
                            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun moveToLandingPage() {
        val intent = Intent(this, LandingPage::class.java) // Adjust to your landing page activity
        startActivity(intent)
        finish()
    }

    private fun validateInputs(): Boolean {
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

