package com.project.pengelolakeuangan.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun savePdfToDownloads(context: Context, fileName: String, pdfData: ByteArray) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // API Level 29 (Android 10) dan lebih tinggi
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/") // Folder Downloads
        }

        val contentResolver = context.contentResolver
        val uri: Uri? = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        uri?.let {
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(pdfData)
                    outputStream.flush()
                }
                Toast.makeText(context, "PDF berhasil disimpan di Downloads", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal menyimpan PDF", Toast.LENGTH_LONG).show()
            }
        }
    } else {
        // Untuk API Level lebih rendah (di bawah Android 10)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(pdfData)
                outputStream.flush()
            }
            Toast.makeText(context, "PDF berhasil disimpan di Downloads", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal menyimpan PDF", Toast.LENGTH_LONG).show()
        }
    }
}
