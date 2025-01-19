package com.project.pengelolakeuangan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.pengelolakeuangan.ui.navigation.MainScreen
import com.project.pengelolakeuangan.ui.navigation.Screen
import com.project.pengelolakeuangan.ui.screens.splash.SplashScreen
import com.project.pengelolakeuangan.ui.screens.splash.WelcomeScreens
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.AppPreferences

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val viewModel: TransaksiViewModel = viewModel()
            val appPreferences = AppPreferences(this)
            var showSplashScreen by remember { mutableStateOf(true) }


            // Mengakses currentRoute di dalam Composable
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry.value?.destination?.route

            // BackHandler untuk menangani back press di MainActivity
            val context = LocalContext.current
            BackHandler {
                if (currentRoute == Screen.Home.route) {
                    Log.d("BackHandler", "User is on Home screen, exiting app.")
                    (context as? Activity)?.finish() // Keluar aplikasi
                } else {
                    Log.d("BackHandler", "Navigating back to Home screen and clearing previous screens.")
                    navController.navigate(Screen.Home.route) {
                        // Menghapus seluruh stack navigasi sebelumnya
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }


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
