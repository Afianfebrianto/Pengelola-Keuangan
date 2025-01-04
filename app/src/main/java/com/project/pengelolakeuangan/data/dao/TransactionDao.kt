package com.project.pengelolakeuangan.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertPemasukan(pemasukan: Pemasukan)

    @Insert
    suspend fun insertPengeluaran(pengeluaran: Pengeluaran)

    // Mendapatkan semua data pemasukan
    @Query("SELECT * FROM pemasukan")
    suspend fun getAllPemasukan(): List<Pemasukan>

    // Mendapatkan semua data pengeluaran
    @Query("SELECT * FROM pengeluaran")
    suspend fun getAllPengeluaran(): List<Pengeluaran>

    // Mendapatkan total pemasukan
    @Query("SELECT SUM(nominal) FROM pemasukan")
    suspend fun getTotalPemasukan(): Double?

    // Mendapatkan total pengeluaran
    @Query("SELECT SUM(nominal) FROM pengeluaran")
    suspend fun getTotalPengeluaran(): Double?

    // Mendapatkan semua pemasukan berdasarkan bulan
    @Query("SELECT * FROM pemasukan WHERE strftime('%m', tanggal) = :month AND strftime('%Y', tanggal) = :year")
    suspend fun getPemasukanByMonth(month: String, year: String): List<Pemasukan>

    // Mendapatkan semua pengeluaran berdasarkan bulan
    @Query("SELECT * FROM pengeluaran WHERE strftime('%m', tanggal) = :month AND strftime('%Y', tanggal) = :year")
    suspend fun getPengeluaranByMonth(month: String, year: String): List<Pengeluaran>

    // Mendapatkan total pemasukan berdasarkan bulan
    @Query("SELECT SUM(nominal) FROM pemasukan WHERE strftime('%m', tanggal) = :month AND strftime('%Y', tanggal) = :year")
    suspend fun getTotalPemasukanByMonth(month: String, year: String): Double?

    // Mendapatkan total pengeluaran berdasarkan bulan
    @Query("SELECT SUM(nominal) FROM pengeluaran WHERE strftime('%m', tanggal) = :month AND strftime('%Y', tanggal) = :year")
    suspend fun getTotalPengeluaranByMonth(month: String, year: String): Double?

}