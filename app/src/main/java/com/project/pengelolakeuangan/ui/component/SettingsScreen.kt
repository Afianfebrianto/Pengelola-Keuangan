package com.project.pengelolakeuangan.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.clearUserData
import com.project.pengelolakeuangan.utils.poppinsFamily

@Composable
fun SettingsScreen(viewModel: TransaksiViewModel, navController: NavController) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }

    // Dialog konfirmasi hapus data
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah Anda yakin ingin menghapus semua data?", fontFamily = poppinsFamily) },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Hapus data di SharedPreferences dan database
                        clearUserData(context, viewModel)
                        openDialog.value = false
                    }
                ) {
                    Text("Ya", fontFamily = poppinsFamily)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog.value = false }
                ) {
                    Text("Tidak", fontFamily = poppinsFamily)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header dengan ikon kembali
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali"
                )
            }
            Text(
                text = "Pengaturan Akun",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 8.dp),
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol untuk hapus semua data
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openDialog.value = true }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete), // Ganti dengan ikon sampah Anda
                contentDescription = "Hapus Semua Data",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Hapus Semua Data",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f),
                fontFamily = poppinsFamily
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arahkan"
            )
        }
    }
}