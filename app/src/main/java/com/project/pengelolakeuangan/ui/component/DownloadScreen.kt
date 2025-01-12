package com.project.pengelolakeuangan.ui.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
                    .background(
                        Color.LightGray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
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
                    .background(
                        Color.LightGray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
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
                        Toast.makeText(
                            context,
                            "Periode maksimal hanya 1 tahun!",
                            Toast.LENGTH_LONG
                        )
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