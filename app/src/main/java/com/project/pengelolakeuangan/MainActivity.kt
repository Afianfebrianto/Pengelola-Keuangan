package com.project.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.project.pengelolakeuangan.data.AppDatabaseProvider
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.ui.theme.PengelolaKeuanganTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    private lateinit var databaseProvider: AppDatabaseProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseProvider = AppDatabaseProvider(this)

        val formatterTanggal = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = LocalDate.now().format(formatterTanggal)

        val formatterWaktu = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = LocalTime.now().format(formatterWaktu)

        val examplePemasukan = Pemasukan(
            tanggal = formattedDate,
            waktu = formattedTime,
            metode = "Transfer Bank",
            sumberPemasukan = "Gaji",
            catatan = "Gaji bulan Desember",
            nominal = 1000000.0
        )

        lifecycleScope.launch {
            databaseProvider.pemasukanDao.insertPemasukan(examplePemasukan)
            val allPemasukan = databaseProvider.pemasukanDao.getAllPemasukan().first() // Mengambil data
            allPemasukan.forEach { pemasukan ->
                Log.d("Pemasukan", "Tanggal: ${pemasukan.tanggal}, Waktu: ${pemasukan.waktu}")
            }
        }


//        enableEdgeToEdge()
//        setContent {
//            PengelolaKeuanganTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
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