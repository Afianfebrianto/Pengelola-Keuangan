package com.project.pengelolakeuangan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pemasukan")
data class Pemasukan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long, // Gunakan tipe Long untuk menyimpan timestamp
    val metode: String, // Contoh: "Transfer Bank", "Tunai"
    val sumberPemasukan: String,
    val catatan: String?,
    val nominal: Double
)
