package com.project.pengelolakeuangan

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
import com.project.pengelolakeuangan.ui.screens.transaksi.TransaksiViewModel
import com.project.pengelolakeuangan.ui.theme.PengelolaKeuanganTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val navController = rememberNavController()
//            MainScreen(navController = navController)

            // Menyiapkan NavController untuk navigasi antar screen
            val navController = rememberNavController()

            // Menyediakan ViewModel untuk digunakan di seluruh aplikasi
            val viewModel: TransaksiViewModel = viewModel()

            // Menyediakan MainScreen dan passing navController
            MainScreen(navController = navController, viewModel = viewModel)
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