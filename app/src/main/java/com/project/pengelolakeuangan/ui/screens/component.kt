package com.project.pengelolakeuangan.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.screens.beranda.TransactionItem
import com.project.pengelolakeuangan.ui.screens.transaksi.TransactionData
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun DonutChart(pemasukan: Double, pengeluaran: Double) {
    val total = pemasukan
    val pengeluaranPercentage = if (total > 0) (pengeluaran / total).toFloat() else 0f
    val pemasukanPercentage = 1f - pengeluaranPercentage // Sisanya adalah pemasukan
//    val pemasukanPercentage = if (pengeluaran >0)( 1f - pengeluaranPercentage) else 0f // Sisanya adalah pemasukan

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2
            val innerRadius = radius * 0.45f // Ukuran radius bagian tengah

            // Draw Pengeluaran Segment (Red)
            drawArc(
                color = Color.Red,
                startAngle = -90f,
                sweepAngle = 360f * pengeluaranPercentage,
                useCenter = true,
                size = size.copy(width = radius * 2, height = radius * 2)
            )

            // Draw Pemasukan Segment (Green)
            drawArc(
                color = Color.Green,
                startAngle = -90f + 360f * pengeluaranPercentage,
                sweepAngle = 360f * pemasukanPercentage,
                useCenter = true,
                size = size.copy(width = radius * 2, height = radius * 2)
            )

            // Draw the inner circle to create the "hole"
            drawCircle(
                color = Color.White,
                radius = innerRadius,
                center = Offset(centerX, centerY)
            )

            // Draw percentage text for Pengeluaran
            val pengeluaranText = "%.1f%%".format(pengeluaranPercentage * 100)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    pengeluaranText,
                    centerX + (radius * 0.7f) * Math.cos(Math.toRadians((-90f + (360f * pengeluaranPercentage) / 2).toDouble())).toFloat(),
                    centerY + (radius * 0.7f) * Math.sin(Math.toRadians((-90f + (360f * pengeluaranPercentage) / 2).toDouble())).toFloat(),
                    Paint().apply {
                        color = Color.White.toArgb()
                        textSize = 40f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

            // Draw percentage text for Pemasukan
            val pemasukanText = "%.1f%%".format(pemasukanPercentage * 100)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    pemasukanText,
                    centerX + (radius * 0.7f) * Math.cos(Math.toRadians((-90f + 360f * pengeluaranPercentage + (360f * pemasukanPercentage) / 2).toDouble())).toFloat(),
                    centerY + (radius * 0.7f) * Math.sin(Math.toRadians((-90f + 360f * pengeluaranPercentage + (360f * pemasukanPercentage) / 2).toDouble())).toFloat(),
                    Paint().apply {
                        color = Color.White.toArgb()
                        textSize = 40f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

@Composable
fun FinancialSummary(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
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
                        text = "${formatToRupiahh(totalIncome)}",
                        style = MaterialTheme.typography.h6,
                        color = Color(0xFF4CAF50)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Pengeluaran", style = MaterialTheme.typography.body1)
                    Text(
                        text = "${formatToRupiahh(totalExpense)}",
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
                    text = "${formatToRupiahh(balance)}",
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}



@Composable
fun SearchScreen(
    transactions: List<TransactionData>,
    onBackClick: () -> Unit
) {


    // State untuk menyimpan query pencarian
    var searchQuery by remember { mutableStateOf("") }

    // Filter transaksi berdasarkan pencarian
    val filteredTransactions = transactions.filter {
        it.detail.contains(searchQuery, ignoreCase = true) ||
                it.note.contains(searchQuery, ignoreCase = true)
    }

    // Scaffold untuk layout dasar dengan top bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pencarian", color = Color.White) },
                backgroundColor = Color(0xFFD81B60),
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        // Column untuk konten utama
        Column(modifier = Modifier.padding(paddingValues)) {
            // TextField untuk memasukkan kata kunci pencarian
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Mulai Mengetik...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                },
                singleLine = true
            )

            // Jika tidak ada transaksi yang ditemukan, tampilkan pesan
            if (filteredTransactions.isEmpty() && searchQuery.isNotEmpty()) {
                Text(
                    text = "Tidak ada hasil yang ditemukan",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                // Menampilkan daftar transaksi yang difilter
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredTransactions) { transaction ->
                        // Menampilkan item transaksi menggunakan TransactionItem
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val dummyTransactions = listOf(
        TransactionData(
            isIncome = true,
            nominal = 5000000,
            date = LocalDate.now(),
            method = "Transfer Bank",
            detail = "Gaji Bulanan",
            note = "Gaji bulan Januari"
        ),
        TransactionData(
            isIncome = false,
            nominal = 150000,
            date = LocalDate.now(),
            method = "Tunai",
            detail = "Belanja",
            note = "Belanja bulanan di minimarket"
        )
    )
    SearchScreen(transactions = dummyTransactions, onBackClick = {})
}


fun formatToRupiah(amount: Int): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}

fun formatToRupiahh(amount: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}

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
        Text(text = "Pilih Periode\nRentang periode maksimum 1 tahun.", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Dari*", style = MaterialTheme.typography.body2)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
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
                .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                .clickable { showDatePicker.value = true to "end" }
                .padding(16.dp)
        ) {
            Text(text = endDate.value.format(dateFormatter))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isValidPeriod) {
                    onDownloadClick(
                        startDate.value.format(dateFormatter),
                        endDate.value.format(dateFormatter)
                    )
                } else {
                    Toast.makeText(context, "Periode maksimal hanya 1 tahun!", Toast.LENGTH_LONG).show()
                }
            },
            enabled = isValidPeriod,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = if (isValidPeriod) Color(0xFFE91E63) else Color.Gray)
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



fun createPDF(context: Context, startDate: String, endDate: String) {
    // Header dan tabel PDF
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Ukuran A4 (595x842 px)
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()

    // **Header**
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 20f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("Laporan Keuangan", pageInfo.pageWidth / 2f, 50f, paint)

    // Logo (Opsional)
    val logo = BitmapFactory.decodeResource(context.resources, R.drawable.kucingg) // Ganti dengan logo aplikasi Anda
    val resizedLogo = Bitmap.createScaledBitmap(logo, 100, 100, false)
    canvas.drawBitmap(resizedLogo, 30f, 30f, paint)

    // Periode transaksi
    paint.textAlign = Paint.Align.LEFT
    paint.textSize = 16f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    canvas.drawText("Periode: $startDate - $endDate", 30f, 150f, paint)

    // **Total Transaksi**
    val totalPemasukan = 1000000.0 // Ganti dengan nilai dari database
    val totalPengeluaran = 750000.0 // Ganti dengan nilai dari database
    val totalSaldo = totalPemasukan - totalPengeluaran

    paint.textSize = 14f
    canvas.drawText("Total Pemasukan: Rp$totalPemasukan", 30f, 180f, paint)
    canvas.drawText("Total Pengeluaran: Rp$totalPengeluaran", 30f, 200f, paint)
    canvas.drawText("Saldo Akhir: Rp$totalSaldo", 30f, 220f, paint)

    // **Tabel Pemasukan**
    paint.textSize = 14f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("Tabel Pemasukan", 30f, 260f, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    var yPosition = 290f
    canvas.drawText("Tanggal", 30f, yPosition, paint)
    canvas.drawText("Metode", 100f, yPosition, paint)
    canvas.drawText("Sumber", 200f, yPosition, paint)
    canvas.drawText("Catatan", 300f, yPosition, paint)
    canvas.drawText("Nominal", 450f, yPosition, paint)

    yPosition += 20f
    val pemasukanData = listOf(
        listOf("05/11/2024", "Transfer", "Gaji", "November", "Rp5.000.000"),
        listOf("06/11/2024", "Tunai", "Bonus", "-", "Rp2.000.000")
    ) // Ganti dengan data dari database
    for (row in pemasukanData) {
        canvas.drawText(row[0], 30f, yPosition, paint)
        canvas.drawText(row[1], 100f, yPosition, paint)
        canvas.drawText(row[2], 200f, yPosition, paint)
        canvas.drawText(row[3], 300f, yPosition, paint)
        canvas.drawText(row[4], 450f, yPosition, paint)
        yPosition += 20f
    }

    // **Tabel Pengeluaran**
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("Tabel Pengeluaran", 30f, yPosition + 30f, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    yPosition += 60f
    canvas.drawText("Tanggal", 30f, yPosition, paint)
    canvas.drawText("Metode", 100f, yPosition, paint)
    canvas.drawText("Tujuan", 200f, yPosition, paint)
    canvas.drawText("Catatan", 300f, yPosition, paint)
    canvas.drawText("Nominal", 450f, yPosition, paint)

    yPosition += 20f
    val pengeluaranData = listOf(
        listOf("07/11/2024", "Transfer", "Belanja", "-", "Rp1.500.000"),
        listOf("08/11/2024", "Tunai", "Makan", "Dinner", "Rp500.000")
    ) // Ganti dengan data dari database
    for (row in pengeluaranData) {
        canvas.drawText(row[0], 30f, yPosition, paint)
        canvas.drawText(row[1], 100f, yPosition, paint)
        canvas.drawText(row[2], 200f, yPosition, paint)
        canvas.drawText(row[3], 300f, yPosition, paint)
        canvas.drawText(row[4], 450f, yPosition, paint)
        yPosition += 20f
    }

    // Selesaikan halaman
    pdfDocument.finishPage(page)

    // Tentukan direktori dan pastikan direktori ada
    val fileDir = File(context.getExternalFilesDir(null), "Laporan_Transaksi_${startDate}_to_${endDate}")
    if (!fileDir.exists()) {
        val dirCreated = fileDir.mkdirs() // Membuat direktori jika belum ada
        if (!dirCreated) {
            Toast.makeText(context, "Gagal membuat direktori", Toast.LENGTH_LONG).show()
            return
        }
    }

    val pdfFile = File(fileDir, "Laporan_Transaksi_${startDate}_to_${endDate}.pdf")

    try {
        // Menulis file PDF
        pdfDocument.writeTo(FileOutputStream(pdfFile))
        Toast.makeText(context, "PDF berhasil disimpan di ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Gagal menyimpan PDF", Toast.LENGTH_LONG).show()
    }

    // Menutup dokumen PDF setelah selesai
    pdfDocument.close()
}








//fun generatePDF(
//    context: Context,
//    transactions: List<TransactionData>,
//    startDate: String,
//    endDate: String,
//    totalIncome: Double,
//    totalExpense: Double
//) {
//
//    fun Double.formatToRupiah(): String {
//        return String.format("%,.0f", this).replace(",", ".")
//    }
//    // Direktori penyimpanan PDF
//    val directoryPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
//    val fileName = "Laporan_Keuangan_${startDate}_to_${endDate}.pdf"
//    val filePath = "$directoryPath/$fileName"
//
//    val pdfWriter = PdfWriter(FileOutputStream(File(filePath)))
//    val pdfDocument = PdfDocument(pdfWriter)
//    val document = Document(pdfDocument, PageSize.A4)
//
//    // Header dengan nama aplikasi dan logo
//    val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 2f))).useAllAvailableWidth()
//    val logoPath = "path/to/logo.png" // Ganti dengan path logo Anda
//    val logoImage = Image(ImageDataFactory.create(logoPath)).setWidth(50f).setHeight(50f)
//
//    headerTable.addCell(Cell().add(logoImage).setBorder(Border.NO_BORDER))
//    headerTable.addCell(
//        Cell().add(Paragraph("Laporan Keuangan").setFontSize(18f).setBold())
//            .setTextAlignment(TextAlignment.RIGHT)
//            .setBorder(Border.NO_BORDER)
//    )
//    document.add(headerTable)
//
//    // Periode transaksi
//    document.add(
//        Paragraph("Periode Transaksi: $startDate - $endDate")
//            .setFontSize(12f)
//            .setMarginTop(10f)
//            .setMarginBottom(20f)
//    )
//
//    // Ringkasan total pemasukan, pengeluaran, dan saldo
//    val totalTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f, 1f))).useAllAvailableWidth()
//    totalTable.addCell(Cell().add(Paragraph("Total Pemasukan").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY))
//    totalTable.addCell(Cell().add(Paragraph("Total Pengeluaran").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY))
//    totalTable.addCell(Cell().add(Paragraph("Saldo").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY))
//
//    val balance = totalIncome - totalExpense
//    totalTable.addCell(Cell().add(Paragraph("Rp ${totalIncome.formatToRupiah()}")))
//    totalTable.addCell(Cell().add(Paragraph("Rp ${totalExpense.formatToRupiah()}")))
//    totalTable.addCell(Cell().add(Paragraph("Rp ${balance.formatToRupiah()}")))
//    document.add(totalTable)
//
//    // Tabel Transaksi Pengeluaran
//    document.add(Paragraph("Tabel Pengeluaran").setBold().setFontSize(14f).setMarginTop(20f))
//    val expenseTable = Table(UnitValue.createPercentArray(floatArrayOf(2f, 2f, 3f, 2f, 2f))).useAllAvailableWidth()
//    expenseTable.addHeaderCell("Tanggal")
//    expenseTable.addHeaderCell("Metode")
//    expenseTable.addHeaderCell("Tujuan Pengeluaran")
//    expenseTable.addHeaderCell("Catatan")
//    expenseTable.addHeaderCell("Nominal")
//
//    transactions.filter { !it.isIncome }.forEach { transaction ->
//        expenseTable.addCell(transaction.date)
//        expenseTable.addCell(transaction.method)
//        expenseTable.addCell(transaction.detail)
//        expenseTable.addCell(transaction.note ?: "-")
//        expenseTable.addCell("Rp ${transaction.nominal.formatToRupiah()}")
//    }
//    document.add(expenseTable)
//
//    // Tabel Transaksi Pemasukan
//    document.add(Paragraph("Tabel Pemasukan").setBold().setFontSize(14f).setMarginTop(20f))
//    val incomeTable = Table(UnitValue.createPercentArray(floatArrayOf(2f, 2f, 3f, 2f, 2f))).useAllAvailableWidth()
//    incomeTable.addHeaderCell("Tanggal")
//    incomeTable.addHeaderCell("Metode")
//    incomeTable.addHeaderCell("Sumber Pemasukan")
//    incomeTable.addHeaderCell("Catatan")
//    incomeTable.addHeaderCell("Nominal")
//
//    transactions.filter { it.isIncome }.forEach { transaction ->
//        incomeTable.addCell(transaction.date)
//        incomeTable.addCell(transaction.method)
//        incomeTable.addCell(transaction.detail)
//        incomeTable.addCell(transaction.note ?: "-")
//        incomeTable.addCell("Rp ${transaction.nominal.formatToRupiah()}")
//    }
//    document.add(incomeTable)
//
//    document.close()
//}


