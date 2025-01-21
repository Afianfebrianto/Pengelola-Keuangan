package com.project.pengelolakeuangan.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import com.project.pengelolakeuangan.ui.component.DownloadScreen
import com.project.pengelolakeuangan.ui.component.EditTransactionScreen
import com.project.pengelolakeuangan.ui.component.SearchScreen
import com.project.pengelolakeuangan.ui.component.SettingsScreen
import com.project.pengelolakeuangan.ui.component.TransactionFormScreen
import com.project.pengelolakeuangan.ui.screens.beranda.HomeScreen
import com.project.pengelolakeuangan.ui.screens.profile.ProfileScreen
import com.project.pengelolakeuangan.ui.screens.rekap.RekapScreen
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionsScreen
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import createPDF
import kotlinx.coroutines.launch
import java.time.LocalTime

sealed class Screen(val route: String) {
    object Home : Screen("beranda")
    object Transaction : Screen("transaksi")
    object Rekap : Screen("rekap")
    object Account : Screen("profil")
    object Form : Screen("form/{isIncome}") {
        fun createRoute(isIncome: Boolean) = "form/$isIncome"
    }
    object EditTransaction : Screen("edit_transaction_screen/{transactionId}/{isIncome}") {
        fun createRoute(transactionId: Int, isIncome: Boolean): String {
            return "edit_transaction_screen/$transactionId/$isIncome"
        }
    }

    object Search : Screen("search")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: TransaksiViewModel,

    ) {
    // Mendapatkan context hanya di dalam Composable
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route // Jika pertama kali, navigasi ke Welcome
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.Transaction.route) {
            TransactionsScreen({ isIncome ->
                navController.navigate(Screen.Form.createRoute(isIncome))
            }, viewModel = viewModel, navController = navController)
        }

        composable("settings") {
            SettingsScreen(viewModel = viewModel, navController = navController)
        }


//        coba
        composable("download") {
            val coroutineScope = rememberCoroutineScope()
            var isLoading by remember { mutableStateOf(false) } // State untuk loading

            DownloadScreen(
                navController = navController,
                isLoading = isLoading, // Kirim state loading ke layar
                onDownloadClick = { startDate, endDate ->
                    coroutineScope.launch {
                        Log.d("DownloadScreen", "Mulai proses download untuk periode: $startDate - $endDate")
                        isLoading = true // Tampilkan ProgressBar
                        try {
                            val (filteredPemasukan, filteredPengeluaran) = viewModel.getDataForPeriod(
                                startDate,
                                endDate
                            )
                            Log.d(
                                "DownloadScreen",
                                "Data berhasil diambil. Pemasukan: ${filteredPemasukan.size}, Pengeluaran: ${filteredPengeluaran.size}"
                            )

                            if (filteredPemasukan.isEmpty() && filteredPengeluaran.isEmpty()) {
                                Log.d("DownloadScreen", "Tidak ada data untuk periode ini.")
                                Toast.makeText(
                                    context,
                                    "Tidak ada data untuk periode ini.",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@launch
                            }

                            createPDF(
                                context = context,
                                startDate = startDate,
                                endDate = endDate,
                                pemasukanList = filteredPemasukan,
                                pengeluaranList = filteredPengeluaran
                            )
                            Log.d("DownloadScreen", "PDF berhasil dibuat.")
                        } catch (e: Exception) {
                            Log.e("DownloadScreen", "Gagal mengambil data: ${e.message}", e)
                            Toast.makeText(
                                context,
                                "Gagal mengambil data: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            isLoading = false // Sembunyikan ProgressBar
                            Log.d("DownloadScreen", "Proses selesai. isLoading diatur ke false.")
                        }
                    }
                }
            )
        }



//        ori
//        composable("download") {
//            val coroutineScope = rememberCoroutineScope()
//
//            DownloadScreen(
//                navController = navController,
//                onDownloadClick = { startDate, endDate ->
//                    coroutineScope.launch {
//                        try {
//                            val (filteredPemasukan, filteredPengeluaran) = viewModel.getDataForPeriod(
//                                startDate,
//                                endDate
//                            )
//
//                            if (filteredPemasukan.isEmpty() && filteredPengeluaran.isEmpty()) {
//                                Toast.makeText(
//                                    context,
//                                    "Tidak ada data untuk periode ini.",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                return@launch
//                            }
//
//                            createPDF(
//                                context = context,
//                                startDate = startDate,
//                                endDate = endDate,
//                                pemasukanList = filteredPemasukan,
//                                pengeluaranList = filteredPengeluaran
//                            )
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                            Toast.makeText(
//                                context,
//                                "Gagal mengambil data: ${e.message}",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }
//            )
//        }

        composable(Screen.Rekap.route) {
            RekapScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.Account.route) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.Form.route) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getString("isIncome")?.toBoolean() ?: true

            val viewModel: TransaksiViewModel = viewModel()

            TransactionFormScreen(
                isIncome = isIncome,
                onSave = { transaction ->
                    if (isIncome) {
                        val pemasukan = Pemasukan(
                            tanggal = transaction.date.toString(),
                            waktu = LocalTime.now().toString(),
                            metode = transaction.method,
                            sumberPemasukan = transaction.detail,
                            catatan = transaction.note,
                            nominal = transaction.nominal
                        )
                        viewModel.savePemasukan(pemasukan)
                    } else {
                        val pengeluaran = Pengeluaran(
                            tanggal = transaction.date.toString(),
                            waktu = LocalTime.now().toString(),
                            metode = transaction.method,
                            tujuanPengeluaran = transaction.detail,
                            catatan = transaction.note,
                            nominal = transaction.nominal
                        )
                        viewModel.savePengeluaran(pengeluaran)
                    }
                },
                onCancel = {
                    navController.popBackStack() // Kembali ke layar sebelumnya
                }
            )
        }


        composable(Screen.Search.route) {
            val transactions by viewModel.transactions.observeAsState(emptyList())

            SearchScreen(
                navController = navController,
                transactions = transactions,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "edit_transaction_screen/{transactionId}/{isIncome}",
            arguments = listOf(
                navArgument("transactionId") { type = NavType.IntType },
                navArgument("isIncome") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId")
            val isIncome = backStackEntry.arguments?.getBoolean("isIncome") ?: false

            if (transactionId != null) {
                // Fetch transaction data based on transactionId and isIncome
                viewModel.fetchTransactionById(transactionId, isIncome)

                val transaction by viewModel.transaction.observeAsState()

                if (transaction != null) {
                    EditTransactionScreen(
                        isIncome = isIncome, // Pass isIncome to the EditTransactionScreen
                        transaction = transaction!!,
                        onSave = { updatedTransaction ->
                            viewModel.saveTransaction(updatedTransaction)
                            navController.popBackStack() // Navigate back after save
                        },
                        onDelete = { id ->
                            viewModel.removeTransaction(id, transaction!!.isIncome)
                            navController.popBackStack() // Navigate back after delete
                        },
                        onCancel = {
                            navController.popBackStack() // Navigate back without changes
                        }
                    )
                } else {
                    // Show loading text while data is loading
                    Text("Memuat data transaksi...", modifier = Modifier.padding(16.dp))
                }
            } else {
                // Show error message if transactionId is invalid
                Text("ID Transaksi tidak valid", modifier = Modifier.padding(16.dp))
            }
        }






    }
}
