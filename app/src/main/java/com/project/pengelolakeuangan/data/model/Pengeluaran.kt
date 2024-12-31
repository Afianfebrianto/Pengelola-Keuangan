package com.project.pengelolakeuangan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengeluaran")
data class Pengeluaran(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tanggal: String, // Format: "YYYY-MM-DD"
    val waktu: String,   // Format: "HH:mm:ss"
    val metode: String, // Contoh: "Transfer Bank", "Tunai"
    val tujuanPengeluaran: String,
    val catatan: String?,
    val nominal: Double
)
