package com.project.pengelolakeuangan.ui.screens.transaksi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.screens.beranda.DatePickerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TransactionsScreen(navigateToForm: (isIncome: Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Transaksi",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navigateToForm(true) }, // Navigate to Pemasukan form
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.downngreen),
                        contentDescription = "Pemasukan",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Pemasukan", color = Color.White)
                }
            }

            Button(
                onClick = { navigateToForm(false) }, // Navigate to Pengeluaran form
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF44336)),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.upredd),
                        contentDescription = "Pengeluaran",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Pengeluaran", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun TransactionFormScreen(
    isIncome: Boolean,
    onSave: (TransactionData) -> Unit,
    onCancel: () -> Unit,
    viewModel: TransaksiViewModel
) {
    val nominal = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val method = remember { mutableStateOf("Transfer Bank") }
    val typeDetail = remember { mutableStateOf("") }
    val note = remember { mutableStateOf("") }

    val showDatePicker = remember { mutableStateOf(false) }

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

    // Menyimpan data transaksi
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Data Berhasil Disimpan") },
            text = { Text("Transaksi Anda telah berhasil disimpan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onCancel() // Navigasi kembali setelah konfirmasi
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isIncome) "Pemasukan" else "Pengeluaran",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nominal.value,
            onValueChange = { nominal.value = it },
            label = { Text("Nominal") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tanggal: ${selectedDate.value.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
            TextButton(onClick = { showDatePicker.value = true }) {
                Text("Pilih Tanggal")
            }
        }

        DropdownMenu(
            options = listOf("Transfer Bank", "Tunai"),
            selectedOption = method.value,
            onOptionSelected = { method.value = it }
        )

        OutlinedTextField(
            value = typeDetail.value,
            onValueChange = { typeDetail.value = it },
            label = { Text(if (isIncome) "Sumber Pemasukan" else "Tujuan Pengeluaran") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note.value,
            onValueChange = { note.value = it },
            label = { Text("Catatan") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCancel) {
                Text("Batal")
            }
            Button(onClick = {
                val transaction = TransactionData(
                    isIncome = isIncome,
                    nominal = nominal.value.toIntOrNull() ?: 0,
                    date = selectedDate.value,
                    method = method.value,
                    detail = typeDetail.value,
                    note = note.value
                )
                onSave(transaction)
                showDialog.value = true
            }) {
                Text("Simpan")
            }
        }
    }
}



@Composable
fun DropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedOption)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}

data class TransactionData(
    val isIncome: Boolean,
    val nominal: Int,
    val date: LocalDate,
    val method: String,
    val detail: String,
    val note: String
)