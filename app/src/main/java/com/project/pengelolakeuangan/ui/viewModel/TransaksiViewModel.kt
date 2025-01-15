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
import java.time.LocalTime

class TransaksiViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao
    private val _transaction = MutableLiveData<TransactionData?>()
    val transaction: LiveData<TransactionData?> get() = _transaction


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
            id = this.id,
            isIncome = isIncome,
            nominal = this.nominal,
            date = LocalDate.parse(this.tanggal),
            method = this.metode,
            detail = this.sumberPemasukan,
            note = this.catatan ?: ""
        )
    }

    // Extension function untuk mengonversi Pengeluaran ke TransactionData
    fun Pengeluaran.toTransactionData(isIncome: Boolean): TransactionData {
        return TransactionData(
            id = this.id,
            isIncome = isIncome,
            nominal = this.nominal,
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

    // **Fungsi baru: Mengambil transaksi berdasarkan tahun**
    fun getTransactionsByYear(year: Int): List<TransactionData> {
        val filteredTransactions = transactions.value?.filter { transaction ->
            transaction.date.year == year
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


    fun fetchTransactionById(id: Int) {
        viewModelScope.launch {
            val result = getTransactionById(id)
            _transaction.postValue(result)
        }
    }

    fun saveTransaction(transaction: TransactionData) {
        viewModelScope.launch {
            updateTransaction(transaction)
        }
    }

    fun removeTransaction(id: Int, isIncome: Boolean) {
        viewModelScope.launch {
            deleteTransaction(id, isIncome)
        }
    }


    // Fungsi untuk mendapatkan transaksi berdasarkan ID
    suspend fun getTransactionById(id: Int): TransactionData? {
        val pemasukan = transactionDao.getPemasukanById(id)
        if (pemasukan != null) {
            return TransactionData(
                id = pemasukan.id,
                isIncome = true,
                nominal = pemasukan.nominal,  // memastikan nominal menggunakan tipe Double
                date = LocalDate.parse(pemasukan.tanggal),
                method = pemasukan.metode ?: "",  // memastikan tidak null
                detail = pemasukan.sumberPemasukan ?: "",  // memastikan tidak null
                note = pemasukan.catatan ?: ""  // memastikan tidak null
            )
        }

        val pengeluaran = transactionDao.getPengeluaranById(id)
        if (pengeluaran != null) {
            return TransactionData(
                id = pengeluaran.id,
                isIncome = false,
                nominal = pengeluaran.nominal,  // memastikan nominal menggunakan tipe Double
                date = LocalDate.parse(pengeluaran.tanggal),
                method = pengeluaran.metode ?: "",  // memastikan tidak null
                detail = pengeluaran.tujuanPengeluaran ?: "",  // memastikan tidak null
                note = pengeluaran.catatan ?: ""  // memastikan tidak null
            )
        }

        return null
    }

    // Fungsi untuk memperbarui transaksi
    suspend fun updateTransaction(transaction: TransactionData) {
        if (transaction.isIncome) {
            val pemasukan = Pemasukan(
                id = transaction.id,  // pastikan id adalah Int
                tanggal = transaction.date.toString(),
                waktu = LocalTime.now().toString(),
                metode = transaction.method,
                sumberPemasukan = transaction.detail,
                catatan = transaction.note,
                nominal = transaction.nominal.toDouble() // memastikan nominal bertipe Double
            )
            transactionDao.updatePemasukan(pemasukan)
        } else {
            val pengeluaran = Pengeluaran(
                id = transaction.id,  // pastikan id adalah Int
                tanggal = transaction.date.toString(),
                waktu = LocalTime.now().toString(),
                metode = transaction.method,
                tujuanPengeluaran = transaction.detail,
                catatan = transaction.note,
                nominal = transaction.nominal.toDouble() // memastikan nominal bertipe Double
            )
            transactionDao.updatePengeluaran(pengeluaran)
        }
    }

    // Fungsi untuk menghapus transaksi
    suspend fun deleteTransaction(id: Int, isIncome: Boolean) {
        if (isIncome) {
            transactionDao.deletePemasukanById(id)
        } else {
            transactionDao.deletePengeluaranById(id)
        }
    }
}




