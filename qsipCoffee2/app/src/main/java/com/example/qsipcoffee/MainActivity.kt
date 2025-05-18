package com.example.qsipcoffee

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<AppCompatButton>(R.id.loginbutton)
        val nameEditText = findViewById<EditText>(R.id.name)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)

        loginButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save the name in SharedPreferences
                val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("USERNAME", name)
                    apply()
                }

                // Animate button press then start explore activity
                loginButton.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        loginButton.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .withEndAction {
                                val intent = Intent(this, explore::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .start()
                    }
                    .start()
            }
        }
    }
}
