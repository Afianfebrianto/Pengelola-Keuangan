package com.project.pengelolakeuangan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.pengelolakeuangan.ui.theme.poppins_bold

@Composable
fun CustomButton() {
    Button(
        onClick = { /* Handle click */ },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = ButtonDefaults.ContentPadding,
        elevation = ButtonDefaults.buttonElevation(4.dp),
        modifier = androidx.compose.ui.Modifier
            .padding(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFC1CC), // Warna gradasi atas (pink muda)
                        Color(0xFFE091A2)  // Warna gradasi bawah
                    )
                ),
                shape = RoundedCornerShape(50)
            )
    ) {
        Text(
            text = "Yuk, Mulai Sekarang!",
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = poppins_bold

            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomButton() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    ) {
        CustomButton()
    }
}