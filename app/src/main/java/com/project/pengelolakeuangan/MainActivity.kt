package com.project.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.pengelolakeuangan.ui.navigation.MainScreen
import com.project.pengelolakeuangan.ui.screens.splash.WelcomeScreens
import com.project.pengelolakeuangan.ui.theme.PengelolaKeuanganTheme
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.AppPreferences


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val viewModel: TransaksiViewModel = viewModel()
            val appPreferences = AppPreferences(this)
            val isFirstLaunch = appPreferences.isFirstLaunch()

            if (isFirstLaunch) {
                appPreferences.setFirstLaunch(false)  // Tandai sudah diluncurkan
                setContent {
                    WelcomeScreens(onFinish = {
                        // Setelah WelcomeScreen selesai, arahkan ke HomeScreen
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        ) // Mulai MainActivity lagi
                        finish()  // Tutup activity sebelumnya
                    })
                }
            } else {
                setContent { MainScreen(navController = navController, viewModel = viewModel) }
            }

        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PengelolaKeuanganTheme {
        Greeting("Android")
    }
}