package com.project.pengelolakeuangan.ui.screens.rekap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RekapScreen(
){
    Text("Rekap Screen")
}

@Composable
fun PieChart(
    data: Map<String, Float>, // Data untuk grafik, seperti {"Pemasukan": 60f, "Pengeluaran": 40f}
    colors: List<Color>,      // Warna untuk setiap bagian grafik
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()
    val proportions = data.values.map { it / total }
    val sweepAngles = proportions.map { 360 * it }

    Canvas(modifier = modifier) {
        var startAngle = 0f
        for ((index, sweepAngle) in sweepAngles.withIndex()) {
            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true, // Mengisi lingkaran dari tengah
                size = Size(size.minDimension, size.minDimension),
                topLeft = Offset(
                    (size.width - size.minDimension) / 2,
                    (size.height - size.minDimension) / 2
                )
            )
            startAngle += sweepAngle
        }
    }
}



// Helper Composable for Stats
@Composable
fun StatCard(title: String, amount: Double, color: Color) {
    Card(
        modifier = Modifier
//            .weight(1f)
            .padding(horizontal = 4.dp),
        backgroundColor = color.copy(alpha = 0.1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.subtitle1)
            Text(text = "Rp${amount}", style = MaterialTheme.typography.h6, color = color)
        }
    }
}
