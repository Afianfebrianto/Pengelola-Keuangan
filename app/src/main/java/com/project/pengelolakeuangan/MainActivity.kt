package com.project.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.pengelolakeuangan.ui.navigation.MainScreen
import com.project.pengelolakeuangan.ui.navigation.Screen
import com.project.pengelolakeuangan.ui.screens.splash.SplashScreen
import com.project.pengelolakeuangan.ui.screens.splash.WelcomeScreens
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.AppPreferences

class MainActivity : ComponentActivity() {
    private var lastBackPressedTime: Long = 0L
    private val exitTimeInterval = 2000L // Interval 2 detik
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            val navController = rememberNavController()
            navController = rememberNavController()
            val viewModel: TransaksiViewModel = viewModel()
            val appPreferences = AppPreferences(this)
            var showSplashScreen by remember { mutableStateOf(true) }

            if (showSplashScreen) {
                SplashScreen(onFinish = {
                    showSplashScreen = false
                })
            } else {
                val isFirstLaunch = appPreferences.isFirstLaunch()

                if (isFirstLaunch) {
                    WelcomeScreens(onFinish = {
                        appPreferences.setFirstLaunch(false) // Set isFirstLaunch ke false setelah tombol diklik
                        startActivity(Intent(this, MainActivity::class.java)) // Reload MainActivity
                        finish()
                    })
                } else {
                    MainScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressedTime <= exitTimeInterval) {
            // Keluar dari aplikasi jika tombol back ditekan dua kali dalam interval
            super.onBackPressed()
        } else {
            lastBackPressedTime = currentTime
            val currentRoute = navController.currentBackStackEntry?.destination?.route

            if (currentRoute != Screen.Home.route) {
                // Jika bukan di layar Home, navigasi ke layar Home
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true // Hapus semua layar sebelumnya
                    }
                }
            } else {
                // Jika sudah di layar Home, tampilkan Toast
                Toast.makeText(this, "Tekan kembali sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
            }
        }
    }

}





