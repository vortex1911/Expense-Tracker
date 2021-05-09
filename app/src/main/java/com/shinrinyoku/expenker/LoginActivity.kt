package com.shinrinyoku.cosker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.shinrinyoku.expenker.HomeActivity
import com.shinrinyoku.expenker.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.editTextUserName.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                signIn(email, password)
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "Enter username and password", Toast.LENGTH_LONG).show()
            }
        }

        binding.registerButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            val email = binding.editTextUserName.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isNotBlank() && password.isNotBlank()) {
                registerUser(email, password)
            } else {
                binding.progressBar.visibility = View.INVISIBLE

                Toast.makeText(this, "Enter username and password", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                Log.d("Login Activity", "Inside task")
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.progressBar.visibility = View.INVISIBLE


                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.progressBar.visibility = View.INVISIBLE

                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_LONG).show()

                }
            })
    }


}


