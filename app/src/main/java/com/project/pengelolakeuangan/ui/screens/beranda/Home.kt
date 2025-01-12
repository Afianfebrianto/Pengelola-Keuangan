package com.project.pengelolakeuangan.ui.screens.beranda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.pengelolakeuangan.ui.component.DatePickerDialog
import com.project.pengelolakeuangan.ui.component.FinancialSummary
import com.project.pengelolakeuangan.ui.component.TransactionItem
import com.project.pengelolakeuangan.ui.navigation.Screen
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun HomeScreen(navController: NavHostController, viewModel: TransaksiViewModel) {
    // Mengamati transaksi yang ada di ViewModel
    val transactions by viewModel.transactions.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getAllTransactions()  // Memanggil getAllTransactions saat HomeScreen pertama kali ditampilkan
    }

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")

    val showDatePicker = remember { mutableStateOf(false) }

    val totalIncome = transactions.filter { it.isIncome }.sumOf { it.nominal }
    val totalExpense = transactions.filter { !it.isIncome }.sumOf { it.nominal }
    val balance = totalIncome - totalExpense

    val filteredTransactions = transactions.filter { transaction ->
        transaction.date == selectedDate.value
    }

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
        // Header dengan Latar Belakang Pink
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF48FB1)) // Pink color sesuai gambar
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tombol Navigasi Kiri
                IconButton(onClick = {
                    selectedDate.value = selectedDate.value.minusDays(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Day",
                        tint = Color.White
                    )
                }

                // Tanggal dengan Button
                TextButton(onClick = { showDatePicker.value = true }) {
                    Text(
                        text = selectedDate.value.format(dateFormatter),
                        color = Color.White,
                        style = MaterialTheme.typography.h6
                    )
                }

                // Tombol Navigasi Kanan
                IconButton(onClick = {
                    selectedDate.value = selectedDate.value.plusDays(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Day",
                        tint = Color.White
                    )
                }

                // Tombol Search
                IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }
        }

        // Ringkasan Keuangan
        FinancialSummary(
            totalIncome = totalIncome.toDouble(),
            totalExpense = totalExpense.toDouble(),
            balance = balance.toDouble()
        )

        // Daftar Transaksi Terbaru
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredTransactions) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }
    }
}







