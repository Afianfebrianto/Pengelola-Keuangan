package com.project.pengelolakeuangan.ui.screens.rekap

import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.pengelolakeuangan.ui.screens.DonutChart
import com.project.pengelolakeuangan.ui.screens.FinancialSummary
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun RekapScreen(pemasukan: Double, pengeluaran: Double, onMonthSelected: (Month) -> Unit) {
    val saldo = pemasukan - pengeluaran

//    Column(modifier = Modifier.fillMaxSize()) {
//        // Header
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {

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
            RekapHeader(onMonthSelected = onMonthSelected)
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
fun RekapHeader(onMonthSelected: (Month) -> Unit) {
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
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Download",
                tint = Color.Black
            )
        }

        // Month Picker
        MonthPicker(onMonthSelected = onMonthSelected)

        // Search Icon
        IconButton(onClick = { /* TODO: Implement search action */ }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun MonthPicker(onMonthSelected: (Month) -> Unit) {
    val months = Month.values()
    val currentMonth = remember { Calendar.getInstance().get(Calendar.MONTH) }
    var selectedMonth = months[currentMonth]

    DropdownMenu(
        expanded = false,
        onDismissRequest = { /* TODO: Handle dismiss */ }
    ) {
        months.forEach { month ->
            DropdownMenuItem(
                text = { Text(month.getDisplayName(TextStyle.FULL, Locale.getDefault())) },
                onClick = {
                    selectedMonth = month
                    onMonthSelected(month)
                }
            )
        }
    }

    Text(
        text = selectedMonth.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable { /* TODO: Show Dropdown */ },
        fontSize = 16.sp
    )
}





