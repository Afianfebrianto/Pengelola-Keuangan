package com.project.pengelolakeuangan.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


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


