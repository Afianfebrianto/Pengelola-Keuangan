package com.project.pengelolakeuangan.ui.screens.transaksi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.component.TransactionItem
import com.project.pengelolakeuangan.ui.viewModel.TransaksiViewModel
import com.project.pengelolakeuangan.utils.poppinsFamily

@Composable
fun TransactionsScreen(navigateToForm: (isIncome: Boolean) -> Unit, viewModel: TransaksiViewModel) {
    val transactions by viewModel.transactions.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        viewModel.getAllTransactions()  // Memanggil getAllTransactions saat HomeScreen pertama kali ditampilkan
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Transaksi",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 32.dp),
            fontFamily = poppinsFamily
        )

        // Tombol untuk Pemasukan dan Pengeluaran
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navigateToForm(true) }, // Navigate to Pemasukan form
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
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
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.upredd),
                        contentDescription = "Pengeluaran",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Pengeluaran", color = Color.White,fontFamily = poppinsFamily)
                }
            }
        }

        // Menambahkan ruang antara tombol dan daftar transaksi
        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn dipisah dalam Column atau Row sendiri untuk memastikan ruangnya cukup
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(transactions) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}


