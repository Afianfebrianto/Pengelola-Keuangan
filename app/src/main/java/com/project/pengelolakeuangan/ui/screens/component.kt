package com.project.pengelolakeuangan.ui.screens

import android.graphics.Paint
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.pengelolakeuangan.ui.screens.beranda.TransactionItem
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionData
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun DonutChart(pemasukan: Double, pengeluaran: Double) {
    val total = pemasukan
    val pengeluaranPercentage = if (total > 0) (pengeluaran / total).toFloat() else 0f
    val pemasukanPercentage = if (total >0)( 1f - pengeluaranPercentage) else 0f // Sisanya adalah pemasukan

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
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

@Composable
fun SearchScreen(
    transactions: List<TransactionData>,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredTransactions = transactions.filter {
        it.detail.contains(searchQuery, ignoreCase = true) ||
                it.note.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Pencarian",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                backgroundColor = Color(0xFFF48FB1),
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 4.dp // Menambahkan efek elevation
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = {
                        Text("Mulai Mengetik...")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            if (filteredTransactions.isEmpty() && searchQuery.isNotEmpty()) {
                Text(
                    text = "Tidak ada hasil yang ditemukan",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredTransactions) { transaction ->
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

@Composable
fun DownloadScreen(
    navController: NavHostController,
    onDownloadClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val startDate = remember { mutableStateOf(LocalDate.now()) }
    val endDate = remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    val showDatePicker = remember { mutableStateOf(false to "start") } // ("start" or "end")

    val isValidPeriod = startDate.value.plusYears(1).isAfter(endDate.value)
    var progressBarVisible by remember { mutableStateOf(false) } // State untuk ProgressBar

    // Box digunakan untuk menempatkan elemen di tengah layar
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // ProgressBar yang akan ditampilkan saat proses download berlangsung
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Download Rekapan Transaksi",
                    style = MaterialTheme.typography.h6
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Pilih Periode\nRentang periode maksimum 1 tahun.",
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Dari*", style = MaterialTheme.typography.body2)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                    .clickable { showDatePicker.value = true to "start" }
                    .padding(16.dp)
            ) {
                Text(text = startDate.value.format(dateFormatter))
            }

            Text(text = "Sampai*", style = MaterialTheme.typography.body2)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                    .clickable { showDatePicker.value = true to "end" }
                    .padding(16.dp)
            ) {
                Text(text = endDate.value.format(dateFormatter))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isValidPeriod) {
                        progressBarVisible = true
                        // Ambil data berdasarkan periode dari ViewModel
                        onDownloadClick(
                            startDate.value.toString(),
                            endDate.value.toString()
                        )
                        progressBarVisible = false
                        Toast.makeText(context, "PDF berhasil dibuat!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Periode maksimal hanya 1 tahun!", Toast.LENGTH_LONG)
                            .show()
                    }
                },
                enabled = isValidPeriod,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isValidPeriod) Color(0xFFE91E63) else Color.Gray
                )
            ) {
                Text(text = "Download", color = Color.White)
            }

            if (showDatePicker.value.first) {
                DatePickerDialog(
                    selectedDate = if (showDatePicker.value.second == "start") startDate.value else endDate.value,
                    onDateSelected = { selectedDate ->
                        if (showDatePicker.value.second == "start") {
                            startDate.value = selectedDate
                            if (endDate.value.isBefore(startDate.value)) {
                                endDate.value = startDate.value.plusDays(1)
                            }
                        } else {
                            endDate.value = selectedDate
                            if (endDate.value.isBefore(startDate.value)) {
                                startDate.value = endDate.value.minusDays(1)
                            }
                        }
                        showDatePicker.value = false to ""
                    },
                    onDismissRequest = { showDatePicker.value = false to "" }
                )
            }
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



