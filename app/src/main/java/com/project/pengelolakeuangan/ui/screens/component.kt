package com.project.pengelolakeuangan.ui.screens

import android.graphics.Paint
import android.icu.text.NumberFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

@Composable
fun DonutChart(pemasukan: Double, pengeluaran: Double) {
    val total = pemasukan + pengeluaran
    val pemasukanPercentage = if (total > 0) (pemasukan / total).toFloat() else 0f
    val pengeluaranPercentage = if (total > 0) (pengeluaran / total).toFloat() else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2
            val innerRadius = radius * 0.45f // Ukuran radius bagian tengah

            // Draw Pengeluaran Segment (Red)
            drawArc(
                color = Color.Red,
                startAngle = -90f,
                sweepAngle = 360f * pengeluaranPercentage,
                useCenter = true,
                size = size.copy(width = radius * 2, height = radius * 2)
            )

            // Draw Pemasukan Segment (Green)
            drawArc(
                color = Color.Green,
                startAngle = -90f + 360f * pengeluaranPercentage,
                sweepAngle = 360f * pemasukanPercentage,
                useCenter = true,
                size = size.copy(width = radius * 2, height = radius * 2)
            )

            // Draw the inner circle to create the "hole"
            drawCircle(
                color = Color.White,
                radius = innerRadius,
                center = Offset(centerX, centerY)
            )

            // Draw percentage text for Pengeluaran
            val pengeluaranText = "%.1f%%".format(pengeluaranPercentage * 100)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    pengeluaranText,
                    centerX + (radius * 0.7f) * Math.cos(Math.toRadians((-90f + (360f * pengeluaranPercentage) / 2).toDouble())).toFloat(),
                    centerY + (radius * 0.7f) * Math.sin(Math.toRadians((-90f + (360f * pengeluaranPercentage) / 2).toDouble())).toFloat(),
                    Paint().apply {
                        color = Color.White.toArgb()
                        textSize = 40f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

            // Draw percentage text for Pemasukan
            val pemasukanText = "%.1f%%".format(pemasukanPercentage * 100)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    pemasukanText,
                    centerX + (radius * 0.7f) * Math.cos(Math.toRadians((-90f + 360f * pengeluaranPercentage + (360f * pemasukanPercentage) / 2).toDouble())).toFloat(),
                    centerY + (radius * 0.7f) * Math.sin(Math.toRadians((-90f + 360f * pengeluaranPercentage + (360f * pemasukanPercentage) / 2).toDouble())).toFloat(),
                    Paint().apply {
                        color = Color.White.toArgb()
                        textSize = 40f
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

@Composable
fun FinancialSummary(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Pemasukan", style = MaterialTheme.typography.body1)
                    Text(
                        text = "${formatToRupiahh(totalIncome)}",
                        style = MaterialTheme.typography.h6,
                        color = Color(0xFF4CAF50)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Pengeluaran", style = MaterialTheme.typography.body1)
                    Text(
                        text = "${formatToRupiahh(totalExpense)}",
                        style = MaterialTheme.typography.h6,
                        color = Color(0xFFF44336)
                    )
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Saldo", style = MaterialTheme.typography.body1)
                Text(
                    text = "${formatToRupiahh(balance)}",
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

fun formatToRupiah(amount: Int): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}

fun formatToRupiahh(amount: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
    return numberFormat.format(amount)
}
