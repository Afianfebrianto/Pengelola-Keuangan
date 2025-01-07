package com.project.pengelolakeuangan.ui.navigation

import android.widget.Toast
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
    object Home : Screen("beranda")
    object Transaction : Screen("transaksi")
    object Rekap : Screen("rekap")
    object Account : Screen("profil")
    object Form : Screen("form/{isIncome}") {
        fun createRoute(isIncome: Boolean) = "form/$isIncome"
    }
    object Search : Screen("search")
    object Welcome : Screen("welcome")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: TransaksiViewModel,
    modifier: Modifier = Modifier
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
            }, viewModel = viewModel)
        }

        composable("settings") {
            SettingsScreen(viewModel = viewModel)
        }

        composable("download") {
            val coroutineScope = rememberCoroutineScope()

            DownloadScreen(
                navController = navController,
                onDownloadClick = { startDate, endDate ->
                    coroutineScope.launch {
                        try {
                            val (filteredPemasukan, filteredPengeluaran) = viewModel.getDataForPeriod(startDate, endDate)

                            if (filteredPemasukan.isEmpty() && filteredPengeluaran.isEmpty()) {
                                Toast.makeText(context, "Tidak ada data untuk periode ini.", Toast.LENGTH_LONG).show()
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
                            Toast.makeText(context, "Gagal mengambil data: ${e.message}", Toast.LENGTH_LONG).show()
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
                },
                viewModel = viewModel
            )
        }

        composable(Screen.Search.route) {
            val transactions by viewModel.transactions.observeAsState(emptyList())

            SearchScreen(
                transactions = transactions,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
