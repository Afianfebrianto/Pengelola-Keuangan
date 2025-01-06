package com.project.pengelolakeuangan.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pengelolakeuangan.data.model.Pemasukan
import kotlinx.coroutines.flow.Flow

@Dao
interface PemasukanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPemasukan(pemasukan: Pemasukan)

    @Query("SELECT * FROM pemasukan ORDER BY tanggal DESC, waktu DESC")
    fun getAllPemasukan(): Flow<List<Pemasukan>>

    @Query("DELETE FROM pemasukan WHERE id = :id")
    suspend fun deletePemasukanById(id: Int)

    @Query("SELECT * FROM pemasukan WHERE tanggal BETWEEN :startDate AND :endDate")
    fun getPemasukanBetweenDates(startDate: String, endDate: String): List<Pemasukan>

    @Query("SELECT SUM(nominal) FROM pemasukan WHERE tanggal BETWEEN :startDate AND :endDate")
    fun getTotalPemasukan(startDate: String, endDate: String): Double
}
