package com.example.qsipcoffee

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class profile : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Load the name from SharedPreferences instead of intent
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("USERNAME", null)

        val profileText = findViewById<TextView>(R.id.profileName)

        if (userName != null) {
            profileText.text = "Welcome, $userName!"
        } else {
            // Optional: handle if no username is found (e.g., redirect or show message)
            profileText.text = "Welcome!"
        }

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }
}
