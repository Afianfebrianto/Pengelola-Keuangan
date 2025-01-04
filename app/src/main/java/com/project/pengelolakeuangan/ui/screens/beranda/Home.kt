package com.project.pengelolakeuangan.ui.screens.beranda

import android.icu.util.Calendar
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
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.pengelolakeuangan.ui.screens.FinancialSummary
import com.project.pengelolakeuangan.ui.screens.formatToRupiah
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionData
import com.project.pengelolakeuangan.ui.screens.transaksi.TransaksiViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun HomeScreen(navController: NavHostController, viewModel: TransaksiViewModel) {
    // Mengamati transaksi yang ada di ViewModel
    val transactions by viewModel.transactions.observeAsState(emptyList())

    // Pastikan data transaksi sudah ada
    LaunchedEffect(Unit) {
        viewModel.getAllTransactions()  // Memanggil getAllTransactions saat HomeScreen pertama kali ditampilkan
    }

    // State untuk tanggal (default: hari ini)
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")

    // State untuk menampilkan DatePicker
    val showDatePicker = remember { mutableStateOf(false) }

    // Data Dummy untuk pemasukan, pengeluaran, dan saldo
    val totalIncome = transactions.filter { it.isIncome }.sumOf { it.nominal }
    val totalExpense = transactions.filter { !it.isIncome }.sumOf { it.nominal }
    val balance = totalIncome - totalExpense

    // Tampilkan DatePickerDialog jika diperlukan
    if (showDatePicker.value) {
        DatePickerDialog(
            selectedDate = selectedDate.value,
            onDateSelected = {
                selectedDate.value = it
                showDatePicker.value = false
            },
            onDismissRequest = { showDatePicker.value = false }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(40.dp)) // Placeholder untuk mengimbangi tombol back

            // Tanggal Navigator
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    selectedDate.value = selectedDate.value.minusDays(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Day"
                    )
                }

                TextButton(onClick = { showDatePicker.value = true }) {
                    Text(text = selectedDate.value.format(dateFormatter))
                }

                IconButton(onClick = {
                    selectedDate.value = selectedDate.value.plusDays(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Day"
                    )
                }
            }

            IconButton(onClick = { /* Action for search button */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        FinancialSummary(
            totalIncome = totalIncome.toDouble(),
            totalExpense = totalExpense.toDouble(),
            balance = balance.toDouble()
        )

        // Daftar Transaksi Terbaru
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(transactions) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }
    }
}

// Item untuk menampilkan satu transaksi
@Composable
fun TransactionItem(transaction: TransactionData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = transaction.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")))
                Text(text = transaction.method, style = MaterialTheme.typography.body2)
                Text(text = transaction.detail, style = MaterialTheme.typography.body2)
            }

            // Nominal dengan format Rp
            Text(
                text = if (transaction.isIncome) "${formatToRupiah(transaction.nominal)}"
                else "-${formatToRupiah(transaction.nominal)}",
                style = MaterialTheme.typography.h6,
                color = if (transaction.isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}



@Composable
fun DatePickerDialog(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

    val datePickerDialog = android.app.DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            val newDate = LocalDate.of(year, month + 1, dayOfMonth)
            onDateSelected(newDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.setOnDismissListener { onDismissRequest() }
    datePickerDialog.show()
}


