package com.project.pengelolakeuangan.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.pengelolakeuangan.data.dao.PemasukanDao
import com.project.pengelolakeuangan.data.dao.PengeluaranDao
import com.project.pengelolakeuangan.data.model.Pemasukan
import com.project.pengelolakeuangan.data.model.Pengeluaran

@Database(entities = [Pemasukan::class, Pengeluaran::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pemasukanDao(): PemasukanDao
    abstract fun pengeluaranDao(): PengeluaranDao
}

class AppDatabaseProvider(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "finance_db"
    ).build()

    val pemasukanDao: PemasukanDao = database.pemasukanDao()
    val pengeluaranDao: PengeluaranDao = database.pengeluaranDao()
}

