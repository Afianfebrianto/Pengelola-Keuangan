
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

//coba


fun createPDF(
    context: Context,
    startDate: String,
    endDate: String,
    pemasukanList: List<Pemasukan>,
    pengeluaranList: List<Pengeluaran>
) {
    // Menampilkan dialog loading
    val progressDialog = ProgressDialog(context).apply {
        setMessage("Membuat PDF...")
        setCancelable(false)
        show()
    }

    val document = PdfDocument()

    // Ukuran halaman A4
    val pageWidth = 595f // A4 width in points
    val pageHeight = 842f // A4 height in points
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), 1).create()
    val page = document.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint().apply {
        textSize = 12f
        color = Color.BLACK
    }

    val titlePaint = Paint().apply {
        textSize = 16f
        color = Color.BLACK
        isFakeBoldText = true
    }

    // Header
    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.kucingjpg)
    val resizedBitmap = Bitmap.createScaledBitmap(logoBitmap, 100, 100, false)
    canvas.drawBitmap(resizedBitmap, 50f, 50f, paint)
//    canvas.drawBitmap(logoBitmap, 50f, 50f, paint)

    canvas.drawText("Laporan Keuangan", 150f, 70f, titlePaint)
    canvas.drawText("Periode: $startDate - $endDate", 150f, 90f, paint)

    // Total Pemasukan dan Pengeluaran
    val totalPemasukan = pemasukanList.sumOf { it.nominal }
    val totalPengeluaran = pengeluaranList.sumOf { it.nominal }
    canvas.drawText("Total Pemasukan: Rp ${totalPemasukan.formatCurrency()}", 150f, 110f, paint)
    canvas.drawText("Total Pengeluaran: Rp ${totalPengeluaran.formatCurrency()}", 150f, 130f, paint)

    // Posisi awal untuk tabel
    var yOffset = 160f

    // Tabel Pengeluaran
    yOffset = drawTableSection(
        canvas,
        title = "Pengeluaran",
        headers = listOf("Tanggal", "Metode", "Tujuan", "Catatan", "Nominal"),
        data = pengeluaranList.map { listOf(it.tanggal, it.metode, it.tujuanPengeluaran, it.catatan ?: "-", it.nominal.formatCurrency()) },
        yOffset = yOffset,
        pageWidth = pageWidth,
        paint = paint
    )

    // Pemisah antar tabel
    yOffset += 20f

    // Tabel Pemasukan
    yOffset = drawTableSection(
        canvas,
        title = "Pemasukan",
        headers = listOf("Tanggal", "Metode", "Sumber", "Catatan", "Nominal"),
        data = pemasukanList.map { listOf(it.tanggal, it.metode, it.sumberPemasukan, it.catatan ?: "-", it.nominal.formatCurrency()) },
        yOffset = yOffset,
        pageWidth = pageWidth,
        paint = paint
    )

    // Menyelesaikan halaman dan dokumen
    document.finishPage(page)

    // Konversi PDF menjadi ByteArray
    val byteArrayOutputStream = ByteArrayOutputStream()
    try {
        document.writeTo(byteArrayOutputStream)
        val pdfData = byteArrayOutputStream.toByteArray()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 ke atas menggunakan MediaStore untuk menyimpan di folder Downloads
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "Rekap_Transaksi_${startDate}_to_${endDate}.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/") // Folder Downloads
            }

            val contentResolver = context.contentResolver
            val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(pdfData)
                    outputStream.flush()
                }
                Toast.makeText(context, "PDF berhasil disimpan di Downloads", Toast.LENGTH_LONG).show()
                openPDF(context, uri) // Membuka file PDF setelah disimpan
            }
        } else {
            // Untuk API Level lebih rendah (di bawah Android 10)
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Rekap_Transaksi_${startDate}_to_${endDate}.pdf")
            try {
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(pdfData)
                    outputStream.flush()
                }
                Toast.makeText(context, "PDF berhasil disimpan di Downloads", Toast.LENGTH_LONG).show()
                openPDF(context, Uri.fromFile(file)) // Membuka file PDF setelah disimpan
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal menyimpan PDF", Toast.LENGTH_LONG).show()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Gagal membuat PDF", Toast.LENGTH_LONG).show()
    } finally {
        document.close()
        progressDialog.dismiss() // Menutup dialog loading
    }
}

// Fungsi untuk membuka PDF setelah disimpan
fun openPDF(context: Context, fileUri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(fileUri, "application/pdf")
        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    context.startActivity(intent)
}

// Fungsi untuk menggambar tabel dengan judul dan data
fun drawTableSection(
    canvas: Canvas,
    title: String,
    headers: List<String>,
    data: List<List<String>>,
    yOffset: Float,
    pageWidth: Float,
    paint: Paint
): Float {
    var currentYOffset = yOffset
    paint.textSize = 12f

    // Judul Tabel
    canvas.drawText(title, 50f, currentYOffset, paint)
    currentYOffset += 20f

    // Header Tabel
    val columnWidths = calculateColumnWidths(headers, pageWidth)
    drawTableRow(canvas, headers, columnWidths, currentYOffset, paint, isHeader = true)
    currentYOffset += 20f

    // Isi Tabel
    if (data.isNotEmpty()) {
        data.forEach { row ->
            drawTableRow(canvas, row, columnWidths, currentYOffset, paint, isHeader = false)
            currentYOffset += 20f
        }
    } else {
        canvas.drawText("Tidak ada data", 50f, currentYOffset, paint)
        currentYOffset += 20f
    }

    return currentYOffset
}

// Fungsi untuk menggambar baris tabel
fun drawTableRow(
    canvas: Canvas,
    row: List<String>,
    columnWidths: List<Float>,
    yOffset: Float,
    paint: Paint,
    isHeader: Boolean
) {
    val boldPaint = Paint(paint).apply { isFakeBoldText = true }
    var xOffset = 50f

    row.forEachIndexed { index, cell ->
        canvas.drawText(cell, xOffset, yOffset, if (isHeader) boldPaint else paint)
        xOffset += columnWidths[index]
    }
}

// Fungsi untuk menghitung lebar kolom secara proporsional
fun calculateColumnWidths(headers: List<String>, pageWidth: Float): List<Float> {
    val totalWeight = headers.size
    val availableWidth = pageWidth - 100f // Margin kiri dan kanan
    return List(headers.size) { availableWidth / totalWeight }
}

// Fungsi untuk memformat nilai nominal menjadi format mata uang
fun Double.formatCurrency(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return numberFormat.format(this)
}

