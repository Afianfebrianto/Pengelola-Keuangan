package com.project.pengelolakeuangan.utils

import android.content.Context

fun saveName(context: Context, name: String) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("name", name).apply()
}