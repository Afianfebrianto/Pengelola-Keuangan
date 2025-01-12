package com.project.pengelolakeuangan.data

import java.time.LocalDate

data class TransactionData(
    val isIncome: Boolean,
    val nominal: Int,
    val date: LocalDate,
    val method: String,
    val detail: String,
    val note: String
)