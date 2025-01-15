package com.project.pengelolakeuangan.data

import java.time.LocalDate

data class TransactionData(
    val id : Int,
    val isIncome: Boolean,
    val nominal: Double,
    val date: LocalDate,
    val method: String,
    val detail: String,
    val note: String
)