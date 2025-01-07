package com.project.pengelolakeuangan

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.pengelolakeuangan.ui.navigation.MainScreen
import com.project.pengelolakeuangan.ui.screens.splash.WelcomeScreens
import com.project.pengelolakeuangan.ui.screens.transaksi.TransaksiViewModel
import com.project.pengelolakeuangan.ui.theme.PengelolaKeuanganTheme
import com.project.pengelolakeuangan.utils.AppPreferences


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Memeriksa jika izin diberikan
        val writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
        val readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (writePermissionGranted && readPermissionGranted) {
            // Izin diberikan
            Toast.makeText(this, "Izin disetujui", Toast.LENGTH_SHORT).show()
        } else {
            // Izin ditolak
            Toast.makeText(this, "Izin ditolak. Tidak dapat menyimpan file.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        // Meminta izin menggunakan ActivityResult API
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // Meminta izin
//            requestPermissionLauncher.launch(
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//            )
//        }

        setContent {
            val navController = rememberNavController()
            val viewModel: TransaksiViewModel = viewModel()
            val context = LocalContext.current
            val appPreferences = AppPreferences(this)
            val isFirstLaunch = appPreferences.isFirstLaunch()

            if (isFirstLaunch){
                appPreferences.setFirstLaunch(false)  // Tandai sudah diluncurkan
                setContent {
                    WelcomeScreens(onFinish = {
                        // Setelah WelcomeScreen selesai, arahkan ke HomeScreen
                        startActivity(Intent(this, MainActivity::class.java)) // Mulai MainActivity lagi
                        finish()  // Tutup activity sebelumnya
                    })
                }
            }else{
                setContent{ MainScreen(navController = navController, viewModel = viewModel, context = context)}
            }

//            MainScreen(navController = navController, viewModel = viewModel, context = context)
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