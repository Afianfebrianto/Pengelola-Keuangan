package com.project.pengelolakeuangan.ui.screens.beranda

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionData
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun HomeScreen(navController: NavHostController) {
    // State untuk tanggal (default: hari ini)
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")

    // State untuk menampilkan DatePicker
    val showDatePicker = remember { mutableStateOf(false) }

    // Data Dummy untuk pemasukan, pengeluaran, dan saldo
    val totalIncome = 5000000
    val totalExpense = 2000000
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

        // Ringkasan Keuangan
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Gray) // Batas Card
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
                            text = "Rp ${formatToRupiah(totalIncome)}",
                            style = MaterialTheme.typography.h6,
                            color = Color(0xFF4CAF50)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Pengeluaran", style = MaterialTheme.typography.body1)
                        Text(
                            text = "Rp ${formatToRupiah(totalExpense)}",
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
                        text = "Rp ${formatToRupiah(balance)}",
                        style = MaterialTheme.typography.h6,
                        color = Color(0xFF2196F3)
                    )
                }
            }
        }

        // Placeholder untuk body
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Daftar pemasukan/pengeluaran akan ditampilkan di sini.")
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


@Composable
fun DropdownDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dates = listOf("Jum, 29 Nov 2024", "Sab, 30 Nov 2024", "Ming, 1 Des 2024") // Data dummy

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        TextButton(onClick = { expanded = true }) {
            Text(text = selectedDate)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dates.forEach { date ->
                DropdownMenuItem(onClick = {
                    onDateSelected(date)
                    expanded = false
                }) {
                    Text(text = date)
                }
            }
        }
    }
}


@Composable
fun DateNavigator(
    selectedDate: LocalDate,
    onDateChanged: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Tombol Previous Day
        IconButton(onClick = { onDateChanged(selectedDate.minusDays(1)) }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous Day"
            )
        }

        // Dropdown Tanggal
        var expanded by remember { mutableStateOf(false) }
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(text = selectedDate.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // Menampilkan tanggal-tanggal dalam dropdown
                val dates = (0..7).map { selectedDate.minusDays(it.toLong()) } // Data dummy
                dates.forEach { date ->
                    DropdownMenuItem(onClick = {
                        onDateChanged(date)
                        expanded = false
                    }) {
                        Text(text = date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")))
                    }
                }
            }
        }

        // Tombol Next Day
        IconButton(onClick = { onDateChanged(selectedDate.plusDays(1)) }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next Day"
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionData) {
    // Format nominal
    val formattedNominal = if (transaction.isIncome) {
        "+Rp ${formatToRupiah(transaction.nominal)}"
    } else {
        "-Rp ${formatToRupiah(transaction.nominal)}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = transaction.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Metode: ${transaction.method}",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = if (transaction.isIncome) "Sumber Pemasukan: ${transaction.detail}" else "Tujuan Pengeluaran: ${transaction.detail}",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = formattedNominal,
                style = MaterialTheme.typography.h6,
                color = if (transaction.isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}


// Helper function to format number to Rupiah
fun formatToRupiah(amount: Double): String {
    val numberFormat = NumberFormat.getNumberInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}

//fun formatToRupiah(value: Int): String {
//    val formatter = DecimalFormat("#,###")
//    return formatter.format(value)
//}