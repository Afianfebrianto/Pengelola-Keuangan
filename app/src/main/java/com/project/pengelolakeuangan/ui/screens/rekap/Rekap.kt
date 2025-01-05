package com.project.pengelolakeuangan.ui.screens.rekap

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun RekapScreen(pemasukan: Double, pengeluaran: Double, onMonthYearSelected: (Int, Int) -> Unit, selectedMonth: Int, selectedYear: Int, navController: NavHostController) {
    val saldo = pemasukan - pengeluaran

    // Mendapatkan nama bulan
    val monthName = Month.of(selectedMonth + 1).getDisplayName(TextStyle.FULL, Locale.getDefault()) // Menambah 1 untuk mendapatkan bulan yang benar

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RekapHeader(
                onMonthYearSelected = onMonthYearSelected,
                monthName = monthName, // Kirim nama bulan ke header
                navController

            )
        }

        FinancialSummary(
            totalIncome = pemasukan,
            totalExpense = pengeluaran,
            balance = saldo
        )

        // Donut Chart Section
        DonutChart(pemasukan, pengeluaran)
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





