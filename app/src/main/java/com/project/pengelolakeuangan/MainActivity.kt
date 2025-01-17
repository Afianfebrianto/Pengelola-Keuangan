package com.project.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.pengelolakeuangan.ui.navigation.MainScreen
import com.project.pengelolakeuangan.ui.screens.splash.SplashScreen
import com.project.pengelolakeuangan.ui.screens.splash.WelcomeScreens
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.AppPreferences

class MainActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val viewModel: TransaksiViewModel = viewModel()
            val appPreferences = AppPreferences(this)
            var showSplashScreen by remember { mutableStateOf(true) } // State untuk kontrol SplashScreen

            if (showSplashScreen) {
                // Tampilkan SplashScreen
                SplashScreen(onFinish = {
                    showSplashScreen = false // Setelah selesai, sembunyikan SplashScreen
                })
            } else {
                // Periksa apakah aplikasi pertama kali dijalankan
                val isFirstLaunch = appPreferences.isFirstLaunch()

                if (isFirstLaunch) {
                    appPreferences.setFirstLaunch(false) // Tandai sudah diluncurkan
                    WelcomeScreens(onFinish = {
                        // Setelah WelcomeScreen selesai, arahkan ke HomeScreen
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        ) // Mulai MainActivity lagi
                        finish() // Tutup activity sebelumnya
                    })
                } else {
                    // Jika bukan pertama kali, langsung ke MainScreen
                    MainScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
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
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController() // NavController untuk navigasi
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
                        appPreferences.setFirstLaunch(false)
                        startActivity(Intent(this, MainActivity::class.java)) // Reload MainActivity
                        finish()
                    })
                } else {
                    MainScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}




