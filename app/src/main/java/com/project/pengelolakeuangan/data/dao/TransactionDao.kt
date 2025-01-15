package com.project.pengelolakeuangan.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import kotlinx.coroutines.flow.Flow

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

    // Mendapatkan total pemasukan berdasarkan bulan
    @Query("SELECT SUM(nominal) FROM pemasukan WHERE strftime('%m', tanggal) = :month AND strftime('%Y', tanggal) = :year")
    suspend fun getTotalPemasukanByMonth(month: String, year: String): Double?

    // Mendapatkan total pengeluaran berdasarkan bulan
    @Query("SELECT SUM(nominal) FROM pengeluaran WHERE strftime('%m', tanggal) = :month AND strftime('%Y', tanggal) = :year")
    suspend fun getTotalPengeluaranByMonth(month: String, year: String): Double?

    // Menghapus semua data pemasukan
    @Query("DELETE FROM pemasukan")
    suspend fun deleteAllPemasukan()

    // Menghapus semua data pengeluaran
    @Query("DELETE FROM pengeluaran")
    suspend fun deleteAllPengeluaran()

    @Query("SELECT * FROM pemasukan WHERE sumberPemasukan LIKE '%' || :query || '%' OR catatan LIKE '%' || :query || '%'")
    fun searchPemasukan(query: String): Flow<List<Pemasukan>>

    @Query("SELECT * FROM pengeluaran WHERE tujuanPengeluaran LIKE '%' || :query || '%' OR catatan LIKE '%' || :query || '%'")
    fun searchPengeluaran(query: String): Flow<List<Pengeluaran>>

    @Query("SELECT * FROM Pemasukan WHERE tanggal BETWEEN :startDate AND :endDate")
    fun getPemasukanByDate(startDate: String, endDate: String): List<Pemasukan>

    @Query("SELECT * FROM Pengeluaran WHERE tanggal BETWEEN :startDate AND :endDate")
    fun getPengeluaranByDate(startDate: String, endDate: String): List<Pengeluaran>

    @Query("SELECT * FROM pemasukan WHERE id = :id")
    suspend fun getPemasukanById(id: Int): Pemasukan?

    @Query("SELECT * FROM pengeluaran WHERE id = :id")
    suspend fun getPengeluaranById(id: Int): Pengeluaran?

    @Update
    suspend fun updatePemasukan(pemasukan: Pemasukan)

    @Update
    suspend fun updatePengeluaran(pengeluaran: Pengeluaran)

    @Query("DELETE FROM pemasukan WHERE id = :id")
    suspend fun deletePemasukanById(id: Int)

    @Query("DELETE FROM pengeluaran WHERE id = :id")
    suspend fun deletePengeluaranById(id: Int)
}