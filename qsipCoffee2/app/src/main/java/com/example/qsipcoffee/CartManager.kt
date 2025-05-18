package com.example.qsipcoffee

object CartManager {
    private val cartItems = mutableListOf<Int>()

    // Map of image resource IDs to prices
    private val priceMap = mapOf(
        R.drawable.mooowhooo to 12,
        R.drawable.expressooyourselff to 10,
        R.drawable.dailyyiceedd to 12,
        R.drawable.laterunning to 14
    )

    fun addItem(imageResId: Int) {
        if (!cartItems.contains(imageResId)) {
            cartItems.add(imageResId)
        }
    }

    fun removeItem(imageResId: Int) {
        cartItems.remove(imageResId)
    }

    fun getItems(): List<Int> = cartItems

    fun getTotalPrice(): Int {
        return cartItems.sumOf { priceMap[it] ?: 0 }
    }

    fun clearCart() {
        cartItems.clear()
    }
}
