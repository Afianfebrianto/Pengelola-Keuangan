package com.project.pengelolakeuangan.utils

import android.content.Context
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel

fun clearUserData(context: Context, viewModel: TransaksiViewModel) {
    // Hapus data di SharedPreferences
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    viewModel.clearDatabase()
}