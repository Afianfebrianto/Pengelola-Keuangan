package com.project.pengelolakeuangan.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.project.pengelolakeuangan.data.AppDatabase
import com.project.pengelolakeuangan.data.TransactionData
import com.project.pengelolakeuangan.data.dao.TransactionDao
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TransaksiViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao

    init {
        val db = Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java, "transaction-database"
        ).fallbackToDestructiveMigration()
            .build()  // Pastikan menggunakan fallbackToDestructiveMigration untuk memudahkan pengembangan.
        transactionDao = db.transactionDao()
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

    // Fungsi untuk mengambil semua transaksi (pemasukan + pengeluaran)
    private val _transactions = MutableLiveData<List<TransactionData>>()
    val transactions: LiveData<List<TransactionData>> = _transactions

    fun getAllTransactions() {
        viewModelScope.launch {
            // Ambil data pemasukan dan pengeluaran secara asynchronous
            val pemasukanList = transactionDao.getAllPemasukan()
            val pengeluaranList = transactionDao.getAllPengeluaran()

            // Gabungkan dan urutkan transaksi berdasarkan tanggal
            val combinedTransactions = (pemasukanList.map { it.toTransactionData(true) } +
                    pengeluaranList.map { it.toTransactionData(false) })
                .sortedByDescending { it.date }

            // Update LiveData dengan data yang sudah digabung
            _transactions.postValue(combinedTransactions)
        }
    }


    // Extension function untuk mengonversi Pemasukan ke TransactionData
    fun Pemasukan.toTransactionData(isIncome: Boolean): TransactionData {
        return TransactionData(
            isIncome = isIncome,
            nominal = this.nominal.toInt(),
            date = LocalDate.parse(this.tanggal),
            method = this.metode,
            detail = this.sumberPemasukan,
            note = this.catatan ?: ""
        )
    }

    // Extension function untuk mengonversi Pengeluaran ke TransactionData
    fun Pengeluaran.toTransactionData(isIncome: Boolean): TransactionData {
        return TransactionData(
            isIncome = isIncome,
            nominal = this.nominal.toInt(),
            date = LocalDate.parse(this.tanggal),
            method = this.metode,
            detail = this.tujuanPengeluaran,
            note = this.catatan ?: ""
        )
    }

    fun clearDatabase() {
        // Menghapus semua data transaksi dari database
        viewModelScope.launch {
            // Menghapus semua pemasukan dan pengeluaran
            transactionDao.deleteAllPemasukan()
            transactionDao.deleteAllPengeluaran()
        }
    }


    fun getTransactionsByMonth(year: Int, month: Int): List<TransactionData> {
        val filteredTransactions = transactions.value?.filter { transaction ->
            transaction.date.year == year && transaction.date.monthValue == month
        } ?: emptyList()
        return filteredTransactions
    }


    suspend fun getDataForPeriod(
        startDate: String,
        endDate: String
    ): Pair<List<Pemasukan>, List<Pengeluaran>> {
        return withContext(Dispatchers.IO) {
            val pemasukan = transactionDao.getPemasukanByDate(startDate, endDate)
            val pengeluaran = transactionDao.getPengeluaranByDate(startDate, endDate)
            Pair(pemasukan, pengeluaran)
        }
    }


}



