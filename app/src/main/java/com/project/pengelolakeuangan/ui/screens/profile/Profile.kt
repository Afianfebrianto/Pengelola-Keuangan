package com.project.pengelolakeuangan.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.formatToRupiah
import com.project.pengelolakeuangan.utils.getStoredName
import com.project.pengelolakeuangan.utils.saveName

@Composable
fun ProfileScreen(navController: NavController, viewModel: TransaksiViewModel) {
    val context = LocalContext.current
    val transactions by viewModel.transactions.observeAsState(emptyList())

    // State untuk nama dan pengeditan
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var newName by remember { mutableStateOf("") }

    // Ambil total pemasukan dan pengeluaran untuk menghitung saldo
    val totalIncome = transactions.filter { it.isIncome }.sumOf { it.nominal }
    val totalExpense = transactions.filter { !it.isIncome }.sumOf { it.nominal }
    val balance = totalIncome - totalExpense

    // Ambil nama dari SharedPreferences saat composable dipanggil pertama kali
    LaunchedEffect(Unit) {
        name = getStoredName(context)
        newName = name
        viewModel.getAllTransactions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Akun
        Text(
            text = "Akun",
            style = MaterialTheme.typography.h5, // Perbesar header
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Baris nama dengan gambar kucing dan ikon edit
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.kucingg), // Ganti dengan sumber gambar kucing
                contentDescription = "Foto Kucing",
                modifier = Modifier
                    .size(80.dp) // Perbesar gambar kucing
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            if (isEditing) {
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    modifier = Modifier
                        .width(200.dp)
                        .background(Color.Transparent), // Buat TextField transparan
                    textStyle = MaterialTheme.typography.h6, // Perbesar teks
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        textColor = MaterialTheme.colors.onBackground
                    )
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
                    text = "Hi, $name!",
                    style = MaterialTheme.typography.h5, // Perbesar teks nama
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = { isEditing = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Name")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol kekayaan bersih
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC0CB)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.kekayaan_bersih),
                style = MaterialTheme.typography.button,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nominal Kekayaan Bersih
        Text(
            text = formatToRupiah(balance),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Pengaturan Akun
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("settings") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Pengaturan Akun",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pengaturan Akun",
                style = MaterialTheme.typography.body1
            )
        }
    }
}










