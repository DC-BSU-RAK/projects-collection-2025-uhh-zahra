package com.example.myapplication

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var selectedBase: String? = null
    private val selectedFruits = mutableListOf<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val instructButton: ImageButton = findViewById(R.id.instrucButton)
        val blenderOverlay: FrameLayout = findViewById(R.id.blenderOverlay)
        val milkButton: ImageButton = findViewById(R.id.milkButton)
        val waterButton: ImageButton = findViewById(R.id.waterButton)
        val strawberryButton: ImageButton = findViewById(R.id.strawberryButton)
        val bananaButton: ImageButton = findViewById(R.id.bananaButton)
        val mangoButton: ImageButton = findViewById(R.id.mangoButton)
        val mixButton: ImageButton = findViewById(R.id.mixButton)
        val backButton: ImageButton = findViewById(R.id.backButton)

        val mediaPlayer = MediaPlayer.create(this, R.raw.bg)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        fun animateButtonClick(button: ImageButton) {
            button.animate()
                .scaleX(0.85f)
                .scaleY(0.85f)
                .setDuration(100)
                .withEndAction {
                    button.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }

        instructButton.setOnClickListener {
            animateButtonClick(instructButton)
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.activity_popup, null)
            val popupWindow = PopupWindow(popupView, 980, 1700, true)
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 20, 100)
            popupView.findViewById<Button>(R.id.closeButton).setOnClickListener {
                popupWindow.dismiss()
            }
        }

        fun updateBlenderImage() {
            blenderOverlay.removeAllViews()

            val combinedLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            selectedBase?.let { base ->
                val baseImg = ImageView(this)
                val baseRes = when (base) {
                    "milk" -> R.drawable.milk
                    "water" -> R.drawable.water
                    else -> 0
                }
                if (baseRes != 0) {
                    baseImg.setImageResource(baseRes)
                    baseImg.layoutParams = LinearLayout.LayoutParams(180, 180).apply {
                        setMargins(10, 0, 10, 0)
                    }
                    combinedLayout.addView(baseImg)
                }
            }

            selectedFruits.forEach { fruit ->
                val fruitImg = ImageView(this)
                val fruitRes = when (fruit.lowercase()) {
                    "banana" -> R.drawable.banana
                    "mango" -> R.drawable.mango
                    "strawberry" -> R.drawable.strawberry
                    else -> 0
                }
                if (fruitRes != 0) {
                    fruitImg.setImageResource(fruitRes)
                    fruitImg.layoutParams = LinearLayout.LayoutParams(150, 150).apply {
                        setMargins(10, 0, 10, 0)
                    }
                    combinedLayout.addView(fruitImg)
                }
            }

            blenderOverlay.addView(combinedLayout)
        }

        milkButton.setOnClickListener {
            animateButtonClick(milkButton)
            selectedBase = if (selectedBase == "milk") null else "milk"
            waterButton.isEnabled = selectedBase != "milk"
            updateBlenderImage()
        }

        waterButton.setOnClickListener {
            animateButtonClick(waterButton)
            selectedBase = if (selectedBase == "water") null else "water"
            milkButton.isEnabled = selectedBase != "water"
            updateBlenderImage()
        }

        fun toggleFruit(fruit: String, button: ImageButton) {
            animateButtonClick(button)
            if (selectedFruits.contains(fruit)) {
                selectedFruits.remove(fruit)
            } else if (selectedFruits.size < 2) {
                selectedFruits.add(fruit)
            } else {
                Toast.makeText(this, "You can only choose 2 fruits!", Toast.LENGTH_SHORT).show()
            }
            updateBlenderImage()
        }

        strawberryButton.setOnClickListener { toggleFruit("Strawberry", strawberryButton) }
        bananaButton.setOnClickListener { toggleFruit("Banana", bananaButton) }
        mangoButton.setOnClickListener { toggleFruit("Mango", mangoButton) }

        mixButton.setOnClickListener {
            animateButtonClick(mixButton)

            if (selectedBase == null || selectedFruits.size != 2) {
                Toast.makeText(this, "Choose a base and 2 fruits!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fruitCombo = selectedFruits.sorted().joinToString(" + ")
            val base = selectedBase
            val name: String
            val imageRes: Int

            when (fruitCombo to base) {
                "Banana + Mango" to "milk" -> {
                    name = "Tropical Cream Dream"
                    imageRes = R.drawable.tropicalcreamdream
                }
                "Banana + Mango" to "water" -> {
                    name = "Island Splash"
                    imageRes = R.drawable.islandsplash
                }
                "Banana + Strawberry" to "milk" -> {
                    name = "Berry Strawberry Bliss"
                    imageRes = R.drawable.berrystrawberrybliss
                }
                "Banana + Strawberry" to "water" -> {
                    name = "Fresh Berry Twist"
                    imageRes = R.drawable.freshberrytwist
                }
                "Mango + Strawberry" to "milk" -> {
                    name = "Sunset Swirl"
                    imageRes = R.drawable.sunsetswirl
                }
                "Mango + Strawberry" to "water" -> {
                    name = "Tropical Tango"
                    imageRes = R.drawable.tropicaltango
                }
                else -> {
                    name = "Mystery Mix"
                    imageRes = R.drawable.mixmyestry
                }
            }

            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.activity_popup_mix_result2, null)
            val resultText = popupView.findViewById<TextView>(R.id.resultText)
            val drinkImage = popupView.findViewById<ImageView>(R.id.drinkImage)

            resultText.text = "$name\n($fruitCombo with $base)"
            drinkImage.setImageResource(imageRes)

            val popupWindow = PopupWindow(popupView, 1000, 1600, true)
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            popupView.findViewById<ImageButton>(R.id.closeResultButton).setOnClickListener {
                popupWindow.dismiss()
            }
        }

        backButton.setOnClickListener {
            animateButtonClick(backButton)
            selectedBase = null
            selectedFruits.clear()
            milkButton.isEnabled = true
            waterButton.isEnabled = true
            updateBlenderImage()
        }
    }
}
