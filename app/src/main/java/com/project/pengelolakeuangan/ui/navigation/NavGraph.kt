package com.project.pengelolakeuangan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import com.project.pengelolakeuangan.ui.screens.DownloadScreen
import com.project.pengelolakeuangan.ui.screens.SearchScreen
import com.project.pengelolakeuangan.ui.screens.beranda.HomeScreen
import com.project.pengelolakeuangan.ui.screens.profile.ProfileScreen
import com.project.pengelolakeuangan.ui.screens.profile.SettingsScreen
import com.project.pengelolakeuangan.ui.screens.rekap.RekapScreen
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionFormScreen
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionsScreen
import com.project.pengelolakeuangan.ui.screens.transaksi.TransaksiViewModel
import createPDF
import kotlinx.coroutines.launch
import java.time.LocalTime

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Transaction : Screen("transaction")
    object Rekap : Screen("rekap")
    object Account : Screen("profile")
    object Form : Screen("form/{isIncome}") {
        fun createRoute(isIncome: Boolean) = "form/$isIncome"
    }
    object Search : Screen("search")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: TransaksiViewModel,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier
    ) {
        composable(Screen.Home.route) {
            // Pastikan viewModel diteruskan ke HomeScreen
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Transaction.route) {
            TransactionsScreen({ isIncome ->
                navController.navigate(Screen.Form.createRoute(isIncome))
            }, viewModel = viewModel)
        }

        composable("settings") {
            SettingsScreen(viewModel = viewModel)
        }

        composable("download") {
            val context = LocalContext.current

            // Handling the onDownloadClick with coroutine scope
            val coroutineScope = rememberCoroutineScope()

            // Ambil data transaksi
            val viewModel: TransaksiViewModel = viewModel()
            val pemasukanList by viewModel.getAllPemasukan().observeAsState(emptyList())
            val pengeluaranList by viewModel.getAllPengeluaran().observeAsState(emptyList())

            // Pastikan data sudah di-load sebelum melakukan download
            DownloadScreen(
                navController = navController,
                onDownloadClick = { startDate, endDate ->
                    coroutineScope.launch {
                        // Panggil createPDF dengan data pemasukan dan pengeluaran yang benar
                        createPDF(
                            context = context,
                            startDate = startDate,
                            endDate = endDate,
                            pemasukanList = pemasukanList,  // List<Pemasukan>
                            pengeluaranList = pengeluaranList // List<Pengeluaran>
                        )
                    }
                }
            )
        }


        composable(Screen.Rekap.route) {
            RekapScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Account.route) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Form.route) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getString("isIncome")?.toBoolean() ?: true

            // Mendapatkan instance ViewModel
            val viewModel: TransaksiViewModel = viewModel()

            // Menyediakan fungsi penyimpanan ke database
            TransactionFormScreen(
                isIncome = isIncome,
                onSave = { transaction ->
                    // Menyimpan transaksi ke database, berdasarkan jenis transaksi (Pemasukan atau Pengeluaran)
                    if (isIncome) {
                        // Convert TransactionData to Pemasukan
                        val pemasukan = Pemasukan(
                            tanggal = transaction.date.toString(),
                            waktu = LocalTime.now().toString(),
                            metode = transaction.method,
                            sumberPemasukan = transaction.detail,
                            catatan = transaction.note,
                            nominal = transaction.nominal.toDouble()
                        )
                        viewModel.savePemasukan(pemasukan)
                    } else {
                        // Convert TransactionData to Pengeluaran
                        val pengeluaran = Pengeluaran(
                            tanggal = transaction.date.toString(),
                            waktu = LocalTime.now().toString(),
                            metode = transaction.method,
                            tujuanPengeluaran = transaction.detail,
                            catatan = transaction.note,
                            nominal = transaction.nominal.toDouble()
                        )
                        viewModel.savePengeluaran(pengeluaran)
                    }
                },
                onCancel = {
                    navController.popBackStack() // Kembali ke layar sebelumnya
                },
                viewModel = viewModel // Pass the viewModel to the form screen
            )
        }

        // Menambahkan rute untuk SearchScreen
        composable(Screen.Search.route) {
            // Mengambil data transaksi dari viewModel
            val transactions by viewModel.transactions.observeAsState(emptyList()) // Pastikan transactions adalah LiveData atau StateFlow

            // Navigasi ke SearchScreen dan meneruskan transaksi serta onBackClick
            SearchScreen(
                transactions = transactions,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
