
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

//fun createPDFWithLoading(
//    context: Context,
//    startDate: String,
//    endDate: String,
//    pemasukanList: List<Pemasukan>,
//    pengeluaranList: List<Pengeluaran>,
//    progressBarVisible: Boolean // Flag for controlling progress bar visibility
//) {
//    // Create PDF document in background thread
//    AsyncTask.execute {
//        createPDF(
//            context,
//            startDate,
//            endDate,
//            pemasukanList,
//            pengeluaranList
//        )
//
//        // Hide ProgressBar and show a message when the process is done
//        (context as Activity).runOnUiThread {
//            // Hide the ProgressBar and show a Toast after PDF is created
//            progressBarVisible = false
//            Toast.makeText(context, "PDF berhasil dibuat", Toast.LENGTH_LONG).show()
//        }
//    }
//}


fun createPDF(
    context: Context,
    startDate: String,
    endDate: String,
    pemasukanList: List<Pemasukan>,
    pengeluaranList: List<Pengeluaran>
) {
    val document = PdfDocument()

    // Ukuran halaman A4 dengan margin lebih besar untuk memberi ruang
    val pageWidth = 650f  // Lebar kertas
    val pageHeight = 900f  // Tinggi kertas
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), 1).create()
    val page = document.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()
    paint.textSize = 12f
    paint.color = Color.BLACK

    // Header
    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.kucingg) // Ganti dengan ID logo aplikasi
    canvas.drawBitmap(logoBitmap, 50f, 50f, paint) // Logo aplikasi pada posisi (50, 50)
    canvas.drawText("Laporan Keuangan", 200f, 50f, paint)
    canvas.drawText("Periode: $startDate - $endDate", 200f, 70f, paint)

    // Total Pemasukan dan Pengeluaran
    val totalPemasukan = pemasukanList.sumOf { it.nominal }
    val totalPengeluaran = pengeluaranList.sumOf { it.nominal }
    canvas.drawText("Total Pemasukan: Rp ${totalPemasukan.formatCurrency()}", 200f, 90f, paint)
    canvas.drawText("Total Pengeluaran: Rp ${totalPengeluaran.formatCurrency()}", 200f, 110f, paint)

    var yOffset = 140f

    // Tabel Pengeluaran
    canvas.drawText("Pengeluaran:", 100f, yOffset, paint)
    yOffset += 20f
    // Header tabel Pengeluaran
    canvas.drawText("Tanggal", 100f, yOffset, paint)
    canvas.drawText("Metode", 200f, yOffset, paint)
    canvas.drawText("Tujuan Pengeluaran", 300f, yOffset, paint)
    canvas.drawText("Catatan", 450f, yOffset, paint)
    canvas.drawText("Nominal", 550f, yOffset, paint)
    yOffset += 20f

    // Menampilkan setiap data pengeluaran
    if (pengeluaranList.isNotEmpty()) {
        for (pengeluaran in pengeluaranList) {
            // Menggambar setiap baris tanpa border
            drawTableRow(canvas, pengeluaran.tanggal, pengeluaran.metode, pengeluaran.tujuanPengeluaran, pengeluaran.catatan ?: "-", pengeluaran.nominal.formatCurrency(), yOffset)
            yOffset += 20f
        }
    } else {
        canvas.drawText("Tidak ada pengeluaran", 100f, yOffset, paint)
        yOffset += 20f
    }

    // Pemisah antar tabel
    yOffset += 20f

    // Tabel Pemasukan
    canvas.drawText("Pemasukan:", 100f, yOffset, paint)
    yOffset += 20f
    // Header tabel Pemasukan
    canvas.drawText("Tanggal", 100f, yOffset, paint)
    canvas.drawText("Metode", 200f, yOffset, paint)
    canvas.drawText("Sumber Pemasukan", 300f, yOffset, paint)
    canvas.drawText("Catatan", 450f, yOffset, paint)
    canvas.drawText("Nominal", 550f, yOffset, paint)
    yOffset += 20f

    // Menampilkan setiap data pemasukan
    if (pemasukanList.isNotEmpty()) {
        for (pemasukan in pemasukanList) {
            // Menggambar setiap baris tanpa border
            drawTableRow(canvas, pemasukan.tanggal, pemasukan.metode, pemasukan.sumberPemasukan, pemasukan.catatan ?: "-", pemasukan.nominal.formatCurrency(), yOffset)
            yOffset += 20f
        }
    } else {
        canvas.drawText("Tidak ada pemasukan", 100f, yOffset, paint)
        yOffset += 20f
    }

    // Menyelesaikan halaman dan dokumen
    document.finishPage(page)

    // Menyimpan PDF ke file
    val file = File(context.getExternalFilesDir(null), "Rekap_Transaksi_${startDate}_to_${endDate}.pdf")
    try {
        document.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF berhasil dibuat: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Gagal membuat PDF", Toast.LENGTH_LONG).show()
    } finally {
        document.close()
    }
}

// Fungsi untuk menggambar setiap baris dalam tabel tanpa border
fun drawTableRow(canvas: Canvas, tanggal: String, metode: String, tujuan: String, catatan: String, nominal: String, yOffset: Float) {
    val paint = Paint()
    paint.textSize = 12f
    paint.color = Color.BLACK

    // Menyesuaikan posisi teks agar lebih rapi, dengan kolom nominal di tengah
    canvas.drawText(tanggal, 100f, yOffset, paint)
    canvas.drawText(metode, 200f, yOffset, paint)
    canvas.drawText(tujuan, 300f, yOffset, paint)
    canvas.drawText(catatan, 450f, yOffset, paint)

    // Agar nominal berada di posisi yang tepat dan tidak terpotong
    canvas.drawText(nominal, 550f, yOffset, paint)
}

// Fungsi untuk memformat nilai nominal menjadi format mata uang
fun Double.formatCurrency(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return numberFormat.format(this)
}
