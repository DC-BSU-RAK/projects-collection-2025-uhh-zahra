package com.example.qsipcoffee

object WishlistManager {
    private val wishlistItems = mutableListOf<Int>()

    fun addItem(item: Int) {
        if (!wishlistItems.contains(item)) {
            wishlistItems.add(item)
        }
    }

    fun removeItem(item: Int) {
        wishlistItems.remove(item)
    }

    fun getItems(): List<Int> = wishlistItems.toList()

    fun clearAll() {
        wishlistItems.clear()
    }
}
