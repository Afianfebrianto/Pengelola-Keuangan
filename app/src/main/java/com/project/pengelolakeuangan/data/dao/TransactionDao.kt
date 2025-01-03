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

}