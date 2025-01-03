package com.project.pengelolakeuangan.ui.screens.transaksi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.project.pengelolakeuangan.data.AppDatabase
import com.project.pengelolakeuangan.data.dao.TransactionDao
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import kotlinx.coroutines.launch

class TransaksiViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "transaction-database"
    ).build().transactionDao()

    // Mengambil semua data pemasukan
    fun getPemasukan(): LiveData<List<Pemasukan>> {
        return transactionDao.getAllPemasukan()
    }

    // Mengambil semua data pengeluaran
    fun getPengeluaran(): LiveData<List<Pengeluaran>> {
        return transactionDao.getAllPengeluaran()
    }


    // Fungsi untuk menyimpan pemasukan
    fun savePemasukan(pemasukan: Pemasukan) {
        viewModelScope.launch {
            transactionDao.insertPemasukan(pemasukan)
        }
    }

    // Fungsi untuk menyimpan pengeluaran
    fun savePengeluaran(pengeluaran: Pengeluaran) {
        viewModelScope.launch {
            transactionDao.insertPengeluaran(pengeluaran)
        }
    }


}