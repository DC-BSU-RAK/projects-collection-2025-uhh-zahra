package com.example.qsipcoffee

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class shop : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve stored username and display it
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("USERNAME", "Guest") ?: "Guest"
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Welcome, $userName!"

        findViewById<ImageButton>(R.id.backbtn).setOnClickListener {
            startActivity(Intent(this, explore::class.java))
            finish()
        }

        val container = findViewById<LinearLayout>(R.id.cart_items_container)
        val totalItemsTextView = findViewById<TextView>(R.id.totalitemnum)
        val totalPriceTextView = findViewById<TextView>(R.id.totalpricenum)

        val cartItems = CartManager.getItems().toMutableList()

        totalItemsTextView.text = cartItems.size.toString()
        totalPriceTextView.text = CartManager.getTotalPrice().toString()

        container.removeAllViews()

        for (imageResId in cartItems) {
            val itemContainer = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
                gravity = Gravity.CENTER_HORIZONTAL
            }

            val imageView = ImageView(this).apply {
                setImageResource(imageResId)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    450
                )
                scaleType = ImageView.ScaleType.FIT_CENTER
                adjustViewBounds = true
            }

            val cancelButton = Button(this).apply {
                text = "Remove ❌"
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                setOnClickListener {
                    // Animate fade out before removing item
                    itemContainer.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction {
                            CartManager.removeItem(imageResId)
                            container.removeView(itemContainer)
                            totalItemsTextView.text = CartManager.getItems().size.toString()
                            totalPriceTextView.text = CartManager.getTotalPrice().toString()
                        }
                        .start()
                }
            }

            itemContainer.addView(imageView)
            itemContainer.addView(cancelButton)
            container.addView(itemContainer)
        }

        // ORDER BUTTON FUNCTIONALITY WITH ANIMATION
        val orderButton = findViewById<Button>(R.id.button)
        orderButton.setOnClickListener {
            // Animate scale press effect
            orderButton.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    orderButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction {
                            val items = CartManager.getItems()
                            if (items.isNotEmpty()) {
                                AlertDialog.Builder(this)
                                    .setTitle("Order Confirmed")
                                    .setMessage("You have successfully ordered your drinks! 🥤")
                                    .setPositiveButton("OK") { dialog, _ ->
                                        dialog.dismiss()
                                        CartManager.clearCart()
                                        recreate() // Refresh to reflect empty cart
                                    }
                                    .setCancelable(false)
                                    .show()
                            } else {
                                Toast.makeText(this, "Please add items to your cart first.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .start()
                }
                .start()
        }
    }
}
