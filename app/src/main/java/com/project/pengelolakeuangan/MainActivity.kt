package com.project.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.project.pengelolakeuangan.data.AppDatabaseProvider
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import com.project.pengelolakeuangan.ui.theme.PengelolaKeuanganTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var databaseProvider: AppDatabaseProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseProvider = AppDatabaseProvider(this)

        // Contoh input data pemasukan
        val examplePemasukan = Pemasukan(
            date = System.currentTimeMillis(),
            metode = "Transfer Bank",
            sumberPemasukan = "Gaji",
            catatan = "Gaji bulan Desember",
            nominal = 5000000.0
        )

        // Gunakan coroutine untuk operasi database
        lifecycleScope.launch {
            databaseProvider.pemasukanDao.insertPemasukan(examplePemasukan)
            val allPemasukan = databaseProvider.pemasukanDao.getAllPemasukan().first() // Mengambil data
            allPemasukan.forEach { pemasukan ->
                Log.d("Pemasukan", "ID: ${pemasukan.id}, Nominal: ${pemasukan.nominal}")
            }
        }

        // Contoh input data pengeluaran
        val examplePengeluaran = Pengeluaran(
            date = System.currentTimeMillis(),
            metode = "Tunai",
            tujuanPengeluaran = "Belanja bulanan",
            catatan = "Supermarket",
            nominal = 150000.0
        )

        lifecycleScope.launch {
            databaseProvider.pengeluaranDao.insertPengeluaran(examplePengeluaran)
            val allPengeluaran = databaseProvider.pengeluaranDao.getAllPengeluaran().first() // Mengambil data
            allPengeluaran.forEach { pengeluaran ->
                Log.d("Pengeluaran", "ID: ${pengeluaran.id}, Nominal: ${pengeluaran.nominal}")
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