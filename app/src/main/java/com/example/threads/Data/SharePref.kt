package com.example.threads.Data
import android.content.Context

object SharePref {

    fun storeData(
        name: String,
        username: String,
        email: String,
        imageUrl: String,
        context: Context,
        bio: String = "Threads User" // Default bio value
    ) {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = shareReferences.edit()
        editor.putString("name", name)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("imageUrl", imageUrl)
        editor.putString("bio", bio)
        editor.apply()
    }

    fun getName(context: Context): String {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return shareReferences.getString("name", "")!!
    }

    fun getUsername(context: Context): String {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return shareReferences.getString("username", "")!!
    }

    fun getEmail(context: Context): String {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return shareReferences.getString("email", "")!!
    }

    fun getImageUrl(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        var imageUrl = sharedPreferences.getString("imageUrl", "") ?: ""
        if (imageUrl.startsWith("http://")) {
            imageUrl = imageUrl.replace("http://", "https://")
        }
        return imageUrl
    }

    fun getBio(context: Context): String {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return shareReferences.getString("bio", "Threads User") ?: "Threads User"
    }

    // Individual setter methods for updating specific fields

    fun setName(context: Context, name: String) {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = shareReferences.edit()
        editor.putString("name", name)
        editor.apply()
    }

    fun setUsername(context: Context, username: String) {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = shareReferences.edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun setEmail(context: Context, email: String) {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = shareReferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun setImageUrl(context: Context, imageUrl: String) {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = shareReferences.edit()
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun setBio(context: Context, bio: String) {
        val shareReferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = shareReferences.edit()
        editor.putString("bio", bio)
        editor.apply()
    }
}