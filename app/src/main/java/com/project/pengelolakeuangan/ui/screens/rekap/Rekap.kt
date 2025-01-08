package com.project.pengelolakeuangan.ui.screens.rekap

import android.app.DatePickerDialog
import android.icu.util.Calendar
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.navigation.Screen
import com.project.pengelolakeuangan.ui.screens.DonutChart
import com.project.pengelolakeuangan.ui.screens.FinancialSummary
import com.project.pengelolakeuangan.ui.screens.beranda.TransactionItem
import com.project.pengelolakeuangan.ui.screens.transaksi.TransaksiViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


//



//@Composable
//fun RekapScreen(navController: NavHostController, viewModel: TransaksiViewModel) {
//    // State untuk tanggal saat ini (default: bulan dan tahun sekarang)
//    val currentDate = remember { mutableStateOf(LocalDate.now()) }
//
//    // Format untuk menampilkan nama bulan dan tahun
//    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
//
//    // Transaksi yang difilter berdasarkan bulan
//    val transactions = remember(currentDate.value) {
//        viewModel.getTransactionsByMonth(
//            year = currentDate.value.year,
//            month = currentDate.value.monthValue
//        )
//    }
//
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Header untuk memilih bulan dan menambahkan tombol
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                // Tombol Download di pojok kiri atas
//                IconButton(onClick = {
//                    navController.navigate("download")
//                }) {
//                    Icon(
//                        painter = painterResource(R.drawable.download),
//                        contentDescription = "Download"
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(8.dp)) // Jarak antara tombol
//            }
//
//            // Tanggal dan navigasi bulan
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                IconButton(onClick = {
//                    currentDate.value = currentDate.value.minusMonths(1)
//                }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Previous Month"
//                    )
//                }
//
//                Text(
//                    text = currentDate.value.format(monthYearFormatter),
//                    style = MaterialTheme.typography.h6
//                )
//
//                IconButton(onClick = {
//                    currentDate.value = currentDate.value.plusMonths(1)
//                }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowForward,
//                        contentDescription = "Next Month"
//                    )
//                }
//            }
//
//            // Tombol Search di pojok kanan atas
//            IconButton(onClick = {
//                navController.navigate(Screen.Search.route)
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search"
//                )
//            }
//        }
//
//        // Total pemasukan, pengeluaran, dan saldo
//        val totalIncome = transactions.filter { it.isIncome }.sumOf { it.nominal }
//        val totalExpense = transactions.filter { !it.isIncome }.sumOf { it.nominal }
//        val balance = totalIncome - totalExpense
//
//        FinancialSummary(
//            totalIncome = totalIncome.toDouble(),
//            totalExpense = totalExpense.toDouble(),
//            balance = balance.toDouble()
//        )
//        DonutChart(pemasukan = totalIncome.toDouble(), pengeluaran = totalExpense.toDouble())
//
//        // Daftar transaksi
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(transactions) { transaction ->
//                TransactionItem(transaction = transaction)
//            }
//        }
//    }
//}

@Composable
fun RekapScreen(navController: NavHostController, viewModel: TransaksiViewModel) {
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
                    style = MaterialTheme.typography.h6
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
            totalIncome = totalIncome.toDouble(),
            totalExpense = totalExpense.toDouble(),
            balance = balance.toDouble()
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

            // Item transaksi
            items(transactions) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }
    }
}







@Composable
fun RekapHeader(onMonthYearSelected: (Int, Int) -> Unit, monthName: String, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Download Icon
        IconButton(onClick = { /* TODO: Implement download action */ }) {
            Icon(
                painter = painterResource(R.drawable.download),
                contentDescription = "Download",
                tint = Color.Black
            )
        }

        // Tampilkan nama bulan di header
        Text(
            text = "Bulan: $monthName", // Menampilkan nama bulan
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Month-Year Picker
        MonthYearPicker(onMonthYearSelected = onMonthYearSelected)

        // Search Icon
        IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
        }
    }
}


@Composable
fun MonthYearPicker(onMonthYearSelected: (Int, Int) -> Unit) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    // Menyimpan bulan dan tahun yang dipilih
    val selectedMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    val selectedYear = remember { mutableStateOf(calendar.get(Calendar.YEAR)) }

    // Mengatur dialog untuk memilih bulan dan tahun
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        // Membuka dialog untuk memilih bulan dan tahun
        DatePickerDialog(
            context,
            { _, year, month, _ ->
                selectedMonth.value = month
                selectedYear.value = year
                onMonthYearSelected(month, year)
            },
            selectedYear.value,
            selectedMonth.value,
            1 // Mengatur hari sebagai 1 karena kita hanya tertarik pada bulan dan tahun
        ).show()

        openDialog.value = false
    }

    // Teks yang menampilkan bulan dan tahun yang dipilih
    Text(
        text = "${selectedMonth.value + 1} - ${selectedYear.value}", // Menampilkan bulan dan tahun
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable { openDialog.value = true }, // Menampilkan dialog saat diklik
        fontSize = 16.sp
    )
}





