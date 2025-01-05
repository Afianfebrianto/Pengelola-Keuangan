package com.project.pengelolakeuangan.ui.screens

import android.graphics.Paint
import android.icu.text.NumberFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.pengelolakeuangan.ui.screens.beranda.TransactionItem
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionData
import java.time.LocalDate

@Composable
fun DonutChart(pemasukan: Double, pengeluaran: Double) {
    val total = pemasukan
    val pengeluaranPercentage = if (total > 0) (pengeluaran / total).toFloat() else 0f
    val pemasukanPercentage = if (pengeluaran >0)( 1f - pengeluaranPercentage) else 0f // Sisanya adalah pemasukan

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2
            val innerRadius = radius * 0.45f // Ukuran radius bagian tengah

            // Draw Pengeluaran Segment (Red)
            drawArc(
                color = Color.Red,
                startAngle = -90f,
                sweepAngle = 360f * pengeluaranPercentage,
                useCenter = true,
                size = size.copy(width = radius * 2, height = radius * 2)
            )

            // Draw Pemasukan Segment (Green)
            drawArc(
                color = Color.Green,
                startAngle = -90f + 360f * pengeluaranPercentage,
                sweepAngle = 360f * pemasukanPercentage,
                useCenter = true,
                size = size.copy(width = radius * 2, height = radius * 2)
            )

            // Draw the inner circle to create the "hole"
            drawCircle(
                color = Color.White,
                radius = innerRadius,
                center = Offset(centerX, centerY)
            )

            // Draw percentage text for Pengeluaran
            val pengeluaranText = "%.1f%%".format(pengeluaranPercentage * 100)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    pengeluaranText,
                    centerX + (radius * 0.7f) * Math.cos(Math.toRadians((-90f + (360f * pengeluaranPercentage) / 2).toDouble())).toFloat(),
                    centerY + (radius * 0.7f) * Math.sin(Math.toRadians((-90f + (360f * pengeluaranPercentage) / 2).toDouble())).toFloat(),
                    Paint().apply {
                        color = Color.White.toArgb()
                        textSize = 40f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

            // Draw percentage text for Pemasukan
            val pemasukanText = "%.1f%%".format(pemasukanPercentage * 100)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    pemasukanText,
                    centerX + (radius * 0.7f) * Math.cos(Math.toRadians((-90f + 360f * pengeluaranPercentage + (360f * pemasukanPercentage) / 2).toDouble())).toFloat(),
                    centerY + (radius * 0.7f) * Math.sin(Math.toRadians((-90f + 360f * pengeluaranPercentage + (360f * pemasukanPercentage) / 2).toDouble())).toFloat(),
                    Paint().apply {
                        color = Color.White.toArgb()
                        textSize = 40f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

@Composable
fun FinancialSummary(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Pemasukan", style = MaterialTheme.typography.body1)
                    Text(
                        text = "${formatToRupiahh(totalIncome)}",
                        style = MaterialTheme.typography.h6,
                        color = Color(0xFF4CAF50)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Pengeluaran", style = MaterialTheme.typography.body1)
                    Text(
                        text = "${formatToRupiahh(totalExpense)}",
                        style = MaterialTheme.typography.h6,
                        color = Color(0xFFF44336)
                    )
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Saldo", style = MaterialTheme.typography.body1)
                Text(
                    text = "${formatToRupiahh(balance)}",
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

//@Composable
//fun SearchScreen(
//    transactions: List<TransactionData>,
//    onBackClick: () -> Unit
//) {
//    var searchQuery by remember { mutableStateOf("") }
//    val filteredTransactions = transactions.filter {
//        it.detail.contains(searchQuery, ignoreCase = true)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Pencarian", color = Color.White) },
//                backgroundColor = Color(0xFFD81B60),
//                navigationIcon = {
//                    IconButton(onClick = { onBackClick() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(modifier = Modifier.padding(paddingValues)) {
//            TextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                placeholder = { Text("Mulai Mengetik...") },
//                leadingIcon = {
//                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
//                }
//            )
//
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(filteredTransactions) { transaction ->
//                    TransactionItem(transaction)
//                }
//            }
//        }
//    }
//}

@Composable
fun SearchScreen(
    transactions: List<TransactionData>,
    onBackClick: () -> Unit
) {


    // State untuk menyimpan query pencarian
    var searchQuery by remember { mutableStateOf("") }

    // Filter transaksi berdasarkan pencarian
    val filteredTransactions = transactions.filter {
        it.detail.contains(searchQuery, ignoreCase = true) ||
                it.note.contains(searchQuery, ignoreCase = true)
    }

    // Scaffold untuk layout dasar dengan top bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pencarian", color = Color.White) },
                backgroundColor = Color(0xFFD81B60),
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        // Column untuk konten utama
        Column(modifier = Modifier.padding(paddingValues)) {
            // TextField untuk memasukkan kata kunci pencarian
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Mulai Mengetik...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                },
                singleLine = true
            )

            // Jika tidak ada transaksi yang ditemukan, tampilkan pesan
            if (filteredTransactions.isEmpty() && searchQuery.isNotEmpty()) {
                Text(
                    text = "Tidak ada hasil yang ditemukan",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                // Menampilkan daftar transaksi yang difilter
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredTransactions) { transaction ->
                        // Menampilkan item transaksi menggunakan TransactionItem
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val dummyTransactions = listOf(
        TransactionData(
            isIncome = true,
            nominal = 5000000,
            date = LocalDate.now(),
            method = "Transfer Bank",
            detail = "Gaji Bulanan",
            note = "Gaji bulan Januari"
        ),
        TransactionData(
            isIncome = false,
            nominal = 150000,
            date = LocalDate.now(),
            method = "Tunai",
            detail = "Belanja",
            note = "Belanja bulanan di minimarket"
        )
    )
    SearchScreen(transactions = dummyTransactions, onBackClick = {})
}


fun formatToRupiah(amount: Int): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}

fun formatToRupiahh(amount: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}
