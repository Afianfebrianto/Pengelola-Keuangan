package com.project.pengelolakeuangan.ui.screens.profile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.screens.formatToRupiah
import com.project.pengelolakeuangan.ui.screens.transaksi.TransaksiViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: TransaksiViewModel) {
    val context = LocalContext.current
    val transactions by viewModel.transactions.observeAsState(emptyList())

    // State untuk nama yang ditampilkan dan status pengeditan
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var newName by remember { mutableStateOf("") }

    // Ambil total pemasukan dan pengeluaran untuk menghitung saldo
    val totalIncome = transactions.filter { it.isIncome }.sumOf { it.nominal }
    val totalExpense = transactions.filter { !it.isIncome }.sumOf { it.nominal }
    val balance = totalIncome - totalExpense

    // Ambil nama dari SharedPreferences ketika composable pertama kali dipanggil
    LaunchedEffect(Unit) {
        name = getStoredName(context)
        newName = name
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Header Akun
        Text(
            text = "Akun",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Baris nama dengan foto kucing dan ikon edit
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Foto kucing di sebelah kiri nama
            Image(
                painter = painterResource(id = R.drawable.kucingg), // Ganti dengan sumber gambar kucing
                contentDescription = "Foto Kucing",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Nama dan tombol edit di sebelah kanan
            if (isEditing) {
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nama") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    // Simpan nama baru
                    saveName(context, newName)
                    name = newName
                    isEditing = false
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Save Name")
                }
            } else {
                Text(
                    text = "Hi ${name}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { isEditing = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Name")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kekayaan Bersih
        Text(
            text = "Kekayaan Bersih",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.fillMaxWidth()
        )

        // Nominal Kekayaan Bersih
        Text(
            text = formatToRupiah(balance),
            style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Pengaturan Akun
        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Pengaturan Akun")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pengaturan Akun")
        }
    }
}

@Composable
fun SettingsScreen(viewModel: TransaksiViewModel) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }

    // Dialog konfirmasi hapus data
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah Anda yakin ingin menghapus semua data?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Hapus data di SharedPreferences dan database

                        clearUserData(context, viewModel)
                        openDialog.value = false
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog.value = false }
                ) {
                    Text("Tidak")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Button(
            onClick = { openDialog.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hapus Semua Data")
        }
    }
}

fun clearUserData(context: Context, viewModel: TransaksiViewModel) {
    // Hapus data di SharedPreferences
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    viewModel.clearDatabase()
}



fun getStoredName(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("name", "User") ?: "User"
}

fun saveName(context: Context, name: String) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("name", name).apply()
}


