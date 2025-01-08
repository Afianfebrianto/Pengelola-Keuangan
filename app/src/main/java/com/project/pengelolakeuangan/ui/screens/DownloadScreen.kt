package com.project.pengelolakeuangan.ui.screens

//@Composable
//fun DownloadScreen(
//    navController: NavHostController,
//    onDownloadClick: suspend (String, String) -> Unit // Fungsi kini mendukung suspend
//) {
//    val context = LocalContext.current
//    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//
//    val startDate = remember { mutableStateOf(LocalDate.now()) }
//    val endDate = remember { mutableStateOf(LocalDate.now().plusDays(1)) }
//    val showDatePicker = remember { mutableStateOf(false to "start") } // ("start" or "end")
//
//    val isValidPeriod = startDate.value.plusYears(1).isAfter(endDate.value)
//    var progressBarVisible by remember { mutableStateOf(false) } // State untuk ProgressBar
//
//    // Box digunakan untuk menempatkan elemen di tengah layar
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // ProgressBar yang akan ditampilkan saat proses download berlangsung
//        if (progressBarVisible) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = "Download Rekapan Transaksi",
//                    style = MaterialTheme.typography.h6
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//            Text(
//                text = "Pilih Periode\nRentang periode maksimum 1 tahun.",
//                style = MaterialTheme.typography.body1
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(text = "Dari*", style = MaterialTheme.typography.body2)
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
//                    .clickable { showDatePicker.value = true to "start" }
//                    .padding(16.dp)
//            ) {
//                Text(text = startDate.value.format(dateFormatter))
//            }
//
//            Text(text = "Sampai*", style = MaterialTheme.typography.body2)
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
//                    .clickable { showDatePicker.value = true to "end" }
//                    .padding(16.dp)
//            ) {
//                Text(text = endDate.value.format(dateFormatter))
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//                    if (isValidPeriod) {
//                        progressBarVisible = true
//                        // Jalankan coroutine untuk mengelola proses async
//                        CoroutineScope(Dispatchers.IO).launch {
//                            try {
//                                onDownloadClick(
//                                    startDate.value.toString(),
//                                    endDate.value.toString()
//                                )
//                                withContext(Dispatchers.Main) {
//                                    Toast.makeText(context, "PDF berhasil dibuat!", Toast.LENGTH_SHORT).show()
//                                }
//                            } catch (e: Exception) {
//                                withContext(Dispatchers.Main) {
//                                    Toast.makeText(context, "Gagal membuat PDF: ${e.message}", Toast.LENGTH_LONG).show()
//                                }
//                            } finally {
//                                withContext(Dispatchers.Main) {
//                                    progressBarVisible = false
//                                }
//                            }
//                        }
//                    } else {
//                        Toast.makeText(context, "Periode maksimal hanya 1 tahun!", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                },
//                enabled = isValidPeriod,
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = if (isValidPeriod) Color(0xFFE91E63) else Color.Gray
//                )
//            ) {
//                Text(text = "Download", color = Color.White)
//            }
//
//            if (showDatePicker.value.first) {
//                DatePickerDialog(
//                    selectedDate = if (showDatePicker.value.second == "start") startDate.value else endDate.value,
//                    onDateSelected = { selectedDate ->
//                        if (showDatePicker.value.second == "start") {
//                            startDate.value = selectedDate
//                            if (endDate.value.isBefore(startDate.value)) {
//                                endDate.value = startDate.value.plusDays(1)
//                            }
//                        } else {
//                            endDate.value = selectedDate
//                            if (endDate.value.isBefore(startDate.value)) {
//                                startDate.value = endDate.value.minusDays(1)
//                            }
//                        }
//                        showDatePicker.value = false to ""
//                    },
//                    onDismissRequest = { showDatePicker.value = false to "" }
//                )
//            }
//        }
//    }
//}