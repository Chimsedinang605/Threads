package com.example.threads.Data

import android.content.Context


object SharePref {

    fun storeData(
        name : String,
        username : String,
        email : String,
        imageUrl : String,
        context : Context
    ) {
       val shareReferces = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = shareReferces.edit()
        editor.putString("name", name)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getName( context: Context): String {
        val shareReferces = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return shareReferces.getString("name", "")!!
    }

    fun getUsername( context: Context): String {
        val shareReferces = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return shareReferces.getString("username", "")!!
    }

    fun getEmail( context: Context): String {
        val shareReferces = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return shareReferces.getString("email", "")!!
    }

    fun getImageUrl( context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        var imageUrl = sharedPreferences.getString("imageUrl", "") ?: ""
        if (imageUrl.startsWith("http://")) {
            imageUrl = imageUrl.replace("http://", "https://")
        }
        return imageUrl
    }



}