package com.project.pengelolakeuangan.utils

import android.icu.text.NumberFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun formatToRupiah(amount: Int): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}

fun formatToRupiah(amount: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}


fun formatToRupiahh(amount: Long): String {
    // Atur simbol khusus untuk format ini
    val symbols = DecimalFormatSymbols().apply {
        currencySymbol = "Rp "     // Simbol mata uang
        groupingSeparator = ','    // Pemisah ribuan menggunakan koma
        decimalSeparator = '.'     // Pemisah desimal menggunakan titik
    }

    // Format angka tanpa trailing angka desimal
    val decimalFormat = DecimalFormat("¤#,##0.##", symbols)

    // Format angka
    return decimalFormat.format(amount)
}


