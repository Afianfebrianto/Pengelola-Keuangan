package com.project.pengelolakeuangan.data.repository

import com.project.pengelolakeuangan.data.dao.PengeluaranDao
import com.project.pengelolakeuangan.data.model.Pengeluaran
import kotlinx.coroutines.flow.Flow

class PengeluaranRepository(private val pengeluaranDao: PengeluaranDao) {
    val allPengeluaran: Flow<List<Pengeluaran>> = pengeluaranDao.getAllPengeluaran()

    suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        pengeluaranDao.insertPengeluaran(pengeluaran)
    }

    suspend fun deletePengeluaranById(id: Int) {
        pengeluaranDao.deletePengeluaranById(id)
    }
}