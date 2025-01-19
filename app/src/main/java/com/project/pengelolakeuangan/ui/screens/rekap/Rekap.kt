package com.project.pengelolakeuangan.ui.screens.rekap

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.component.DonutChart
import com.project.pengelolakeuangan.ui.component.FinancialSummary
import com.project.pengelolakeuangan.ui.component.TransactionItem
import com.project.pengelolakeuangan.ui.navigation.Screen
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.poppinsFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun RekapScreen0(navController: NavHostController, viewModel: TransaksiViewModel) {
    // State untuk tanggal saat ini (default: bulan dan tahun sekarang)
    val currentDate = remember { mutableStateOf(LocalDate.now()) }

    // Format untuk menampilkan nama bulan dan tahun
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    // Transaksi yang difilter berdasarkan bulan
    val transactions = remember(currentDate.value) {
        viewModel.getTransactionsByMonth(
            year = currentDate.value.year,
            month = currentDate.value.monthValue
        )
    }

    // LazyListState untuk mendeteksi posisi scroll
    val listState = rememberLazyListState()
    val isDonutChartVisible by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header untuk memilih bulan dan menambahkan tombol
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Tombol Download di pojok kiri atas
                IconButton(onClick = {
                    navController.navigate("download")
                }) {
                    Icon(
                        painter = painterResource(R.drawable.download),
                        contentDescription = "Download"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Jarak antara tombol
            }

            // Tanggal dan navigasi bulan
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    currentDate.value = currentDate.value.minusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Month"
                    )
                }

                Text(
                    text = currentDate.value.format(monthYearFormatter),
                    style = MaterialTheme.typography.h6,
                    fontFamily = poppinsFamily
                )

                IconButton(onClick = {
                    currentDate.value = currentDate.value.plusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Month"
                    )
                }
            }

            // Tombol Search di pojok kanan atas
            IconButton(onClick = {
                navController.navigate(Screen.Search.route)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        // Total pemasukan, pengeluaran, dan saldo
        val totalIncome = transactions.filter { it.isIncome }.sumOf { it.nominal }
        val totalExpense = transactions.filter { !it.isIncome }.sumOf { it.nominal }
        val balance = totalIncome - totalExpense

        FinancialSummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance
        )

        // LazyColumn untuk DonutChart dan transaksi
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Item pertama: Donut Chart
            if (isDonutChartVisible) {
                item {
                    DonutChart(
                        pemasukan = totalIncome.toDouble(),
                        pengeluaran = totalExpense.toDouble(),
                    )
                }
            }

//             Item transaksi
            items(transactions) { transaction ->
                TransactionItem(transaction = transaction, onClick = { selectedTransaction ->
                    navController.navigate("edit_transaction_screen/${selectedTransaction.id}")
                })
            }
        }
    }
}

@Composable
fun RekapScreen1(navController: NavHostController, viewModel: TransaksiViewModel) {
    val currentDate = remember { mutableStateOf(LocalDate.now()) }
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val transactions = remember(currentDate.value) {
        viewModel.getTransactionsByMonth(
            year = currentDate.value.year,
            month = currentDate.value.monthValue
        )
    }
    val yearlyTransactions = remember(currentDate.value.year) {
        viewModel.getTransactionsByYear(year = currentDate.value.year)
    }
    val listState = rememberLazyListState()
    val isDonutChartVisible by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    // State untuk dialog
    val isDialogOpen = remember { mutableStateOf(false) }
    val isYearlyReport = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    navController.navigate("download")
                }) {
                    Icon(
                        painter = painterResource(R.drawable.download),
                        contentDescription = "Download"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    currentDate.value = currentDate.value.minusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Month"
                    )
                }

                Text(
                    text = if (isYearlyReport.value)
                        "${currentDate.value.year}" // Format Tahun
                    else
                        currentDate.value.format(monthYearFormatter), // Format Bulan dan Tahun
                    style = MaterialTheme.typography.h6,
                    fontFamily = poppinsFamily
                )

                IconButton(onClick = {
                    currentDate.value = currentDate.value.plusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Month"
                    )
                }
            }

            IconButton(onClick = {
                isDialogOpen.value = true // Buka dialog
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        // Dialog untuk memilih laporan bulanan atau tahunan
        if (isDialogOpen.value) {
            AlertDialog(
                onDismissRequest = { isDialogOpen.value = false },
                title = { Text(text = "Pilih Laporan", fontFamily = poppinsFamily, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        TextButton(onClick = {
                            isYearlyReport.value = false
                            isDialogOpen.value = false
                        }) {
                            Text(text = "Laporan Bulanan", fontFamily = poppinsFamily )
                        }
                        TextButton(onClick = {
                            isYearlyReport.value = true
                            isDialogOpen.value = false
                        }) {
                            Text(text = "Laporan Tahunan" , fontFamily = poppinsFamily)
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }

        val totalIncome = if (isYearlyReport.value) {
            yearlyTransactions.filter { it.isIncome }.sumOf { it.nominal }
        } else {
            transactions.filter { it.isIncome }.sumOf { it.nominal }
        }
        val totalExpense = if (isYearlyReport.value) {
            yearlyTransactions.filter { !it.isIncome }.sumOf { it.nominal }
        } else {
            transactions.filter { !it.isIncome }.sumOf { it.nominal }
        }
        val balance = totalIncome - totalExpense

        FinancialSummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isDonutChartVisible) {
                item {
                    DonutChart(
                        pemasukan = totalIncome.toDouble(),
                        pengeluaran = totalExpense.toDouble(),
                    )
                }
            }

            val transactionList = if (isYearlyReport.value) yearlyTransactions else transactions
            items(transactionList) { transaction ->
                TransactionItem(transaction = transaction, onClick = { selectedTransaction ->
                    navController.navigate("edit_transaction_screen/${selectedTransaction.id}")
                })
            }
        }
    }
}

@Composable
fun RekapScreen(navController: NavHostController, viewModel: TransaksiViewModel) {
    val currentDate = remember { mutableStateOf(LocalDate.now()) }
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val transactions = remember(currentDate.value) {
        viewModel.getTransactionsByMonth(
            year = currentDate.value.year,
            month = currentDate.value.monthValue
        )
    }
    val yearlyTransactions = remember(currentDate.value.year) {
        viewModel.getTransactionsByYear(year = currentDate.value.year)
    }
    val listState = rememberLazyListState()
    val isDonutChartVisible by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    // State untuk dialog
    val isDialogOpen = remember { mutableStateOf(false) }
    val isYearlyReport = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.navigate("download")
            }) {
                Icon(
                    painter = painterResource(R.drawable.download),
                    contentDescription = "Download"
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Tombol Previous
                IconButton(onClick = {
                    currentDate.value = if (isYearlyReport.value) {
                        // Kurangi tahun jika laporan tahunan
                        currentDate.value.minusYears(1)
                    } else {
                        // Kurangi bulan jika laporan bulanan
                        currentDate.value.minusMonths(1)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous"
                    )
                }

                // Teks Header (Bulan atau Tahun)
                Text(
                    text = if (isYearlyReport.value)
                        "${currentDate.value.year}" // Format Tahun
                    else
                        currentDate.value.format(monthYearFormatter), // Format Bulan dan Tahun
                    style = MaterialTheme.typography.h6,
                    fontFamily = poppinsFamily,
                    modifier = Modifier.clickable {
                        isDialogOpen.value = true // Buka dialog saat teks diklik
                    }
                )

                // Tombol Next
                IconButton(onClick = {
                    currentDate.value = if (isYearlyReport.value) {
                        // Tambah tahun jika laporan tahunan
                        currentDate.value.plusYears(1)
                    } else {
                        // Tambah bulan jika laporan bulanan
                        currentDate.value.plusMonths(1)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next"
                    )
                }
            }

            //search
            // Tombol Search di pojok kanan atas
            IconButton(onClick = {
                navController.navigate(Screen.Search.route)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        // Dialog untuk memilih laporan bulanan atau tahunan
        if (isDialogOpen.value) {
            AlertDialog(
                onDismissRequest = { isDialogOpen.value = false },
                title = { Text(text = "Pilih Laporan", fontFamily = poppinsFamily, fontWeight = FontWeight.Bold)},
                text = {
                    Column {
                        TextButton(onClick = {
                            isYearlyReport.value = false // Pilih laporan bulanan
                            isDialogOpen.value = false
                        }) {
                            Text(text = "Laporan Bulanan", fontFamily = poppinsFamily)
                        }
                        TextButton(onClick = {
                            isYearlyReport.value = true // Pilih laporan tahunan
                            isDialogOpen.value = false
                        }) {
                            Text(text = "Laporan Tahunan", fontFamily = poppinsFamily)
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }

        // Total pemasukan, pengeluaran, dan saldo
        val totalIncome = if (isYearlyReport.value) {
            yearlyTransactions.filter { it.isIncome }.sumOf { it.nominal }
        } else {
            transactions.filter { it.isIncome }.sumOf { it.nominal }
        }
        val totalExpense = if (isYearlyReport.value) {
            yearlyTransactions.filter { !it.isIncome }.sumOf { it.nominal }
        } else {
            transactions.filter { !it.isIncome }.sumOf { it.nominal }
        }
        val balance = totalIncome - totalExpense

        FinancialSummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isDonutChartVisible) {
                item {
                    DonutChart(
                        pemasukan = totalIncome.toDouble(),
                        pengeluaran = totalExpense.toDouble(),
                    )
                }
            }

            val transactionList = if (isYearlyReport.value) yearlyTransactions else transactions
            items(transactionList) { transaction ->
                TransactionItem(transaction = transaction,onClick = { selectedTransaction ->
                    navController.navigate(Screen.EditTransaction.createRoute(selectedTransaction.id, selectedTransaction.isIncome))
                })
            }

            Log.d("RekapScreen", "Transaksi Transactions: $transactionList")
        }
    }

}





