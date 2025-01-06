package com.project.pengelolakeuangan.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pengelolakeuangan.data.model.Pengeluaran
import kotlinx.coroutines.flow.Flow

@Dao
interface PengeluaranDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengeluaran(pengeluaran: Pengeluaran)

    @Query("SELECT * FROM pengeluaran ORDER BY tanggal DESC, waktu DESC")
    fun getAllPengeluaran(): Flow<List<Pengeluaran>>

    @Query("DELETE FROM pengeluaran WHERE id = :id")
    suspend fun deletePengeluaranById(id: Int)

    @Query("SELECT * FROM pengeluaran WHERE tanggal BETWEEN :startDate AND :endDate")
    fun getPengeluaranBetweenDates(startDate: String, endDate: String): List<Pengeluaran>

    @Query("SELECT SUM(nominal) FROM pengeluaran WHERE tanggal BETWEEN :startDate AND :endDate")
    fun getTotalPengeluaran(startDate: String, endDate: String): Double
}
