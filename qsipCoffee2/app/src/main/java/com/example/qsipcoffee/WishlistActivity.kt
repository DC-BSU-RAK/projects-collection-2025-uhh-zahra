package com.example.qsipcoffee

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WishlistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wishlist)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, explore::class.java))
            finish()
        }

        val wishlistContainer = findViewById<LinearLayout>(R.id.wishlistContainer)
        wishlistContainer.removeAllViews()

        // Populate wishlist
        WishlistManager.getItems().forEach { imageResId ->
            val itemImage = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 16)
                }
                setImageResource(imageResId)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            wishlistContainer.addView(itemImage)
        }

        val clearButton = findViewById<Button>(R.id.button2)
        clearButton.setOnClickListener {
            val animationDuration = 400L
            val delayBetween = 150L

            val itemCount = wishlistContainer.childCount

            if (itemCount == 0) {
                WishlistManager.clearAll()
                return@setOnClickListener
            }

            for (i in 0 until itemCount) {
                val child = wishlistContainer.getChildAt(i)

                child.postDelayed({
                    val fadeOut = AlphaAnimation(1f, 0f).apply {
                        duration = animationDuration
                        fillAfter = true
                    }

                    // After the last item fades out, clear the wishlist
                    if (i == itemCount - 1) {
                        fadeOut.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationRepeat(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {
                                WishlistManager.clearAll()
                                wishlistContainer.removeAllViews()
                            }
                        })
                    }

                    child.startAnimation(fadeOut)
                }, i * delayBetween)
            }
        }
    }
}
