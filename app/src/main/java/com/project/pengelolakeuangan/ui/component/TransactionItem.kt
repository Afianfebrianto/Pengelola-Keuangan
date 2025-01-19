package com.project.pengelolakeuangan.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.pengelolakeuangan.data.TransactionData
import com.project.pengelolakeuangan.utils.formatToRupiahh
import com.project.pengelolakeuangan.utils.poppinsFamily
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(transaction: TransactionData, onClick: (TransactionData) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(transaction) }, // Tambahkan klik
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = transaction.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")), fontFamily = poppinsFamily)
                Text(text = transaction.method, style = MaterialTheme.typography.body2, fontFamily = poppinsFamily)
                Text(text = transaction.detail, style = MaterialTheme.typography.body2, fontFamily = poppinsFamily)
            }

            // Nominal dengan format Rp
            Text(
                text = if (transaction.isIncome) "${formatToRupiahh(transaction.nominal)}"
                else "-${formatToRupiahh(transaction.nominal)}",
                style = MaterialTheme.typography.h6,
                color = if (transaction.isIncome) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontFamily = poppinsFamily
            )
        }
    }
}
