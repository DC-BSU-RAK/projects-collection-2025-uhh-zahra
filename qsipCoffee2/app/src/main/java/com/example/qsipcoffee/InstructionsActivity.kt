package com.example.qsipcoffee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class InstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions)

        // Optional: Make this a smaller popup-style window
        val window = this.window
        window.setLayout(
            (resources.displayMetrics.widthPixels * 0.20).toInt(),
            (resources.displayMetrics.heightPixels * 0.10).toInt()
        )
    }
}
