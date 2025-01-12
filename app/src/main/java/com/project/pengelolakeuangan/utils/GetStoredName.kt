package com.project.pengelolakeuangan.utils

import android.content.Context

fun getStoredName(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("name", "User") ?: "User"
}