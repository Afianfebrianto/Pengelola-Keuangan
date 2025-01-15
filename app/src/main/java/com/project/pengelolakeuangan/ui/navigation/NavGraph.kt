package com.project.pengelolakeuangan.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
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
    object EditTransaction : Screen("edit_transaction_screen/{transactionId}") {
        fun createRoute(transactionId: Int) = "edit_transaction_screen/$transactionId"
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

        composable("download") {
            val coroutineScope = rememberCoroutineScope()

            DownloadScreen(
                navController = navController,
                onDownloadClick = { startDate, endDate ->
                    coroutineScope.launch {
                        try {
                            val (filteredPemasukan, filteredPengeluaran) = viewModel.getDataForPeriod(
                                startDate,
                                endDate
                            )

                            if (filteredPemasukan.isEmpty() && filteredPengeluaran.isEmpty()) {
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
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                context,
                                "Gagal mengambil data: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
                            nominal = transaction.nominal.toDouble()
                        )
                        viewModel.savePemasukan(pemasukan)
                    } else {
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
            route = Screen.EditTransaction.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId")

            if (transactionId != null) {
                // Fetch transaction data
                viewModel.fetchTransactionById(transactionId)

                val transaction by viewModel.transaction.observeAsState()

                if (transaction != null) {
                    EditTransactionScreen(
                        transaction = transaction!!,
                        onSave = { updatedTransaction ->
                            viewModel.saveTransaction(updatedTransaction)
                            navController.popBackStack() // Kembali ke layar sebelumnya setelah disimpan
                        },
                        onDelete = { id ->
                            viewModel.removeTransaction(id, transaction!!.isIncome)
                            navController.popBackStack() // Kembali ke layar sebelumnya setelah dihapus
                        },
                        onCancel = {
                            navController.popBackStack() // Kembali ke layar sebelumnya
                        }
                    )
                } else {
                    // Menampilkan loading sementara data belum siap
                    Text("Memuat data transaksi...", modifier = Modifier.padding(16.dp))
                }
            } else {
                // Menampilkan pesan kesalahan jika ID tidak valid
                Text("ID Transaksi tidak valid", modifier = Modifier.padding(16.dp))
            }
        }


    }
}
