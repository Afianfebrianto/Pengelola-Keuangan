package com.project.pengelolakeuangan.ui.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.project.pengelolakeuangan.data.TransactionData
import java.time.format.DateTimeFormatter

@Composable
fun EditTransactionScreen1(
    transaction: TransactionData,
    onSave:(TransactionData) -> Unit,
    onDelete: (Int) -> Unit,
    onCancel: () -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val nominal = remember { mutableStateOf(transaction.nominal.toString()) }
    val selectedDate = remember { mutableStateOf(transaction.date) }
    val method = remember { mutableStateOf(transaction.method) }
    val typeDetail = remember { mutableStateOf(transaction.detail) }
    val note = remember { mutableStateOf(transaction.note) }

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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header dengan Cancel dan Save
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Tombol Cancel di kiri atas
            IconButton(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }

            // Text di tengah
            Text(
                text = if (transaction.isIncome) "Edit Pemasukan" else "Edit Pengeluaran",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )

            // Tombol Save di kanan atas
            TextButton(
                onClick = {
                    val nominalValue = nominal.value.toIntOrNull()
                    if (nominalValue == null || nominalValue <= 0) {
                        Toast.makeText(
                            context,
                            "Nominal tidak boleh kosong atau kurang dari 1.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    if (typeDetail.value.isBlank()) {
                        Toast.makeText(
                            context,
                            if (transaction.isIncome) "Sumber pemasukan tidak boleh kosong."
                            else "Tujuan pengeluaran tidak boleh kosong.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    val updatedTransaction = transaction.copy(
                        nominal = nominalValue.toLong(),
                        date = selectedDate.value,
                        method = method.value,
                        detail = typeDetail.value,
                        note = note.value
                    )

                    onSave(updatedTransaction)
                }
            ) {
                Text(text = "Simpan")
            }
        }

        // Konten Formulir
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nominal.value,
                onValueChange = { nominal.value = it },
                label = { Text("Nominal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Tanggal, metode pembayaran, dan lainnya tetap sama
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tanggal: ${selectedDate.value.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
                TextButton(onClick = { showDatePicker.value = true}) {
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
                label = { Text(if (transaction.isIncome) "Sumber Pemasukan" else "Tujuan Pengeluaran") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = note.value,
                onValueChange = { note.value = it },
                label = { Text("Catatan") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tombol Hapus
            TextButton(onClick = {
                onDelete(transaction.id)
            }) {
                Text("Hapus Transaksi", color = Color.Red)
            }
        }
    }
}

@Composable
fun EditTransactionScreen(
    isIncome: Boolean, // Add isIncome parameter to handle the type of transaction
    transaction: TransactionData,
    onSave: (TransactionData) -> Unit,
    onDelete: (Int) -> Unit,
    onCancel: () -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf(transaction.date) }
    val nominal = remember { mutableStateOf("") }
    val method = remember { mutableStateOf("") }
    val typeDetail = remember { mutableStateOf("") }
    val note = remember { mutableStateOf("") }

    LaunchedEffect(transaction) {
        nominal.value = transaction.nominal.toString()
        selectedDate.value = transaction.date
        method.value = transaction.method
        typeDetail.value = transaction.detail
        note.value = transaction.note
    }

    // Show DatePickerDialog if necessary
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }

            Text(
                text = if (isIncome) "Edit Pemasukan" else "Edit Pengeluaran",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )

            TextButton(
                onClick = {
                    val nominalValue = nominal.value.toLongOrNull()
                    if (nominalValue == null || nominalValue <= 0) {
                        Toast.makeText(
                            context,
                            "Nominal tidak boleh kosong atau kurang dari 1.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    if (typeDetail.value.isBlank()) {
                        Toast.makeText(
                            context,
                            if (isIncome) "Sumber pemasukan tidak boleh kosong."
                            else "Tujuan pengeluaran tidak boleh kosong.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    val updatedTransaction = transaction.copy(
                        nominal = nominalValue,
                        date = selectedDate.value,
                        method = method.value,
                        detail = typeDetail.value,
                        note = note.value
                    )

                    onSave(updatedTransaction)
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(text = "Simpan")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            // Tombol Hapus
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = { onDelete(transaction.id) },
                    shape = RoundedCornerShape(50), // Membuat bentuk oval
                    border = BorderStroke(1.dp, Color.Black), // Warna border hitam
                    modifier = Modifier.size(100.dp, 40.dp) // Menyesuaikan ukuran tombol
                ) {
                    Text(
                        text = "Hapus",
                        color = Color.Black, // Warna teks hitam
                        style = MaterialTheme.typography.body2
                    )
                }
            }

        }
    }
}


@Composable
fun EditTransactionScreen11(
    transaction: TransactionData,
    onSave: (TransactionData) -> Unit,
    onDelete: (Int) -> Unit,
    onCancel: () -> Unit,
    isIncome: Boolean // Tambahkan parameter isIncome
) {
    val showDatePicker = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val nominal = remember { mutableStateOf(transaction.nominal.toString()) }
    val selectedDate = remember { mutableStateOf(transaction.date) }
    val method = remember { mutableStateOf(transaction.method) }
    val typeDetail = remember { mutableStateOf(transaction.detail) }
    val note = remember { mutableStateOf(transaction.note) }

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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header dengan Cancel dan Save
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Tombol Cancel di kiri atas
            IconButton(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }

            // Text di tengah
            Text(
                text = if (isIncome) "Edit Pemasukan" else "Edit Pengeluaran",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )

            // Tombol Save di kanan atas
            TextButton(
                onClick = {
                    val nominalValue = nominal.value.toIntOrNull()
                    if (nominalValue == null || nominalValue <= 0) {
                        Toast.makeText(
                            context,
                            "Nominal tidak boleh kosong atau kurang dari 1.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    if (typeDetail.value.isBlank()) {
                        Toast.makeText(
                            context,
                            if (isIncome) "Sumber pemasukan tidak boleh kosong."
                            else "Tujuan pengeluaran tidak boleh kosong.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    val updatedTransaction = transaction.copy(
                        nominal = nominalValue.toLong(),
                        date = selectedDate.value,
                        method = method.value,
                        detail = typeDetail.value,
                        note = note.value
                    )

                    onSave(updatedTransaction)
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(text = "Simpan")
            }
        }

        // Konten Formulir
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            // Tombol Hapus
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = { onDelete(transaction.id) },
                    shape = RoundedCornerShape(50), // Membuat bentuk oval
                    border = BorderStroke(1.dp, Color.Black), // Warna border hitam
                    modifier = Modifier.size(100.dp, 40.dp) // Menyesuaikan ukuran tombol
                ) {
                    Text(
                        text = "Hapus",
                        color = Color.Black, // Warna teks hitam
                        style = MaterialTheme.typography.body2
                    )
                }
            }

        }
    }

    LaunchedEffect(nominal.value, selectedDate.value, method.value, typeDetail.value, note.value) {
        Log.d("EditTransactionScreen", "Nominal: ${nominal.value}")
        Log.d("EditTransactionScreen", "SelectedDate: ${selectedDate.value}")
        Log.d("EditTransactionScreen", "Method: ${method.value}")
        Log.d("EditTransactionScreen", "TypeDetail: ${typeDetail.value}")
        Log.d("EditTransactionScreen", "Note: ${note.value}")
    }

}
