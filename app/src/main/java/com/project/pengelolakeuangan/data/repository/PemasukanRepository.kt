package com.project.pengelolakeuangan.data.repository

import com.project.pengelolakeuangan.data.dao.PemasukanDao
import com.project.pengelolakeuangan.data.model.Pemasukan
import kotlinx.coroutines.flow.Flow

class PemasukanRepository(private val pemasukanDao: PemasukanDao) {
    val allPemasukan: Flow<List<Pemasukan>> = pemasukanDao.getAllPemasukan()

    suspend fun insertPemasukan(pemasukan: Pemasukan) {
        pemasukanDao.insertPemasukan(pemasukan)
    }

    suspend fun deletePemasukanById(id: Int) {
        pemasukanDao.deletePemasukanById(id)
    }
}