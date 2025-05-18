package com.example.qsipcoffee

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class explore : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_explore)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.explorepage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userName = intent.getStringExtra("USERNAME")

        // Navigation buttons
        findViewById<ImageButton>(R.id.profilebtn).setOnClickListener {
            val intent = Intent(this, profile::class.java)
            intent.putExtra("USERNAME", userName)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        findViewById<ImageButton>(R.id.cartbtn).setOnClickListener {
            startActivity(Intent(this, shop::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        findViewById<ImageButton>(R.id.wishlistbtn).setOnClickListener {
            startActivity(Intent(this, WishlistActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Instructions button (popup)
        findViewById<ImageButton>(R.id.imageButton2).setOnClickListener {
            showInstructionsPopup()
        }

        // Wishlist toggles
        setupHeartToggle(R.id.heartbefore, R.drawable.mooowhooo)
        setupHeartToggle(R.id.heartbefore2, R.drawable.expressooyourselff)
        setupHeartToggle(R.id.heartbefore3, R.drawable.dailyyiceedd)
        setupHeartToggle(R.id.heartbefore4, R.drawable.laterunning)

        // Add to cart toggles
        setupPlusToggle(R.id.addbtn, R.drawable.mooowhooo)
        setupPlusToggle(R.id.addbtn2, R.drawable.expressooyourselff)
        setupPlusToggle(R.id.addbtn3, R.drawable.dailyyiceedd)
        setupPlusToggle(R.id.addbtn4, R.drawable.laterunning)
    }

    private fun showInstructionsPopup() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_instructions, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<Button>(R.id.closePopup).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupHeartToggle(buttonId: Int, imageResId: Int) {
        val heartButton = findViewById<ImageButton>(buttonId)

        fun updateHeartImageWithAnimation(isLiked: Boolean) {
            val drawableRes = if (isLiked) R.drawable.heartafter else R.drawable.heartbefore
            heartButton.animate().alpha(0f).setDuration(150).withEndAction {
                heartButton.setImageResource(drawableRes)
                heartButton.animate().alpha(1f).setDuration(150).start()
            }.start()
        }

        val isInitiallyLiked = WishlistManager.getItems().contains(imageResId)
        updateHeartImageWithAnimation(isInitiallyLiked)

        heartButton.setOnClickListener {
            val isLikedNow = WishlistManager.getItems().contains(imageResId)
            if (isLikedNow) {
                WishlistManager.removeItem(imageResId)
            } else {
                WishlistManager.addItem(imageResId)
            }
            updateHeartImageWithAnimation(!isLikedNow)
        }
    }

    private fun setupPlusToggle(buttonId: Int, imageResId: Int) {
        val plusButton = findViewById<ImageButton>(buttonId)

        fun updateButtonImageWithAnimation() {
            val drawableRes = if (CartManager.getItems().contains(imageResId)) {
                R.drawable.afterbtn
            } else {
                R.drawable.plusbefore
            }

            plusButton.animate().alpha(0f).setDuration(150).withEndAction {
                plusButton.setImageResource(drawableRes)
                plusButton.animate().alpha(1f).setDuration(150).start()
            }.start()
        }

        updateButtonImageWithAnimation()

        plusButton.setOnClickListener {
            if (CartManager.getItems().contains(imageResId)) {
                CartManager.removeItem(imageResId)
            } else {
                CartManager.addItem(imageResId)
            }
            updateButtonImageWithAnimation()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
