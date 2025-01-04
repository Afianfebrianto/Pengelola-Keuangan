package com.project.pengelolakeuangan.ui.screens.transaksi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.project.pengelolakeuangan.data.AppDatabase
import com.project.pengelolakeuangan.data.dao.TransactionDao
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month

class TransaksiViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao

    init {
        val db = Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java, "transaction-database"
        ).fallbackToDestructiveMigration().build()  // Pastikan menggunakan fallbackToDestructiveMigration untuk memudahkan pengembangan.
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

    fun getTotalPemasukan(): LiveData<Double> {
        val totalPemasukan = MutableLiveData<Double>()
        viewModelScope.launch {
            val pemasukanList = transactionDao.getAllPemasukan()
            totalPemasukan.postValue(pemasukanList.sumOf { it.nominal })
        }
        return totalPemasukan
    }

    // Mendapatkan total pengeluaran
    fun getTotalPengeluaran(): LiveData<Double> {
        val totalPengeluaran = MutableLiveData<Double>()
        viewModelScope.launch {
            val pengeluaranList = transactionDao.getAllPengeluaran()
            totalPengeluaran.postValue(pengeluaranList.sumOf { it.nominal })
        }
        return totalPengeluaran
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

    fun loadTransactionsForMonth(selectedMonth: Month) {
        viewModelScope.launch {
            val pemasukanFiltered = transactionDao.getAllPemasukan().filter { pemasukan ->
                LocalDate.parse(pemasukan.tanggal).month == selectedMonth
            }
            val pengeluaranFiltered = transactionDao.getAllPengeluaran().filter { pengeluaran ->
                LocalDate.parse(pengeluaran.tanggal).month == selectedMonth
            }

            // Update LiveData dengan data transaksi yang difilter
            val combinedTransactions = (pemasukanFiltered.map { it.toTransactionData(true) } +
                    pengeluaranFiltered.map { it.toTransactionData(false) })
                .sortedByDescending { it.date }

            _transactions.postValue(combinedTransactions)
        }
    }

    fun clearDatabase() {
        // Menghapus semua data transaksi dari database
        viewModelScope.launch {
            // Menghapus semua pemasukan dan pengeluaran
            transactionDao.deleteAllPemasukan()
            transactionDao.deleteAllPengeluaran()
        }
    }

}



