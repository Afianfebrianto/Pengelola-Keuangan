package com.project.pengelolakeuangan.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.pengelolakeuangan.data.dao.TransactionDao
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran

@Database(entities = [Pemasukan::class, Pengeluaran::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}

