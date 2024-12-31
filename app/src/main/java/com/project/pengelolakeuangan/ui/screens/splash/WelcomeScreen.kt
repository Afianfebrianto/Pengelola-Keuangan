package com.project.pengelolakeuangan.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.theme.arimo_reguler
import com.project.pengelolakeuangan.ui.theme.poppins_bold

@Composable
fun WelcomeScreen1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image of the cat
        Image(
            painter = painterResource(id = R.drawable.kucing), // Replace with your image resource
            contentDescription = "Cat Image",
            modifier = Modifier
                .size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text below the image
        Text(
            text = "Halo, Jadikan Keuanganmu\nLebih Terkendali!",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = poppins_bold
            ),
            modifier = Modifier.padding(8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Button
        Button(
            onClick = { /* Handle button click */ },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            contentPadding = PaddingValues(),
            elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFC1CC), // Light pink
                                Color(0xFFE091A2)  // Darker pink
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Yuk, Mulai Sekarang!",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = poppins_bold
                    )
                )
            }
        }
    }
}

@Composable
fun WelcomeScreen2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image of the cat
        Image(
            painter = painterResource(id = R.drawable.kucingg), // Replace with your image resource
            contentDescription = "Cat Image",
            modifier = Modifier
                .size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selamat Datang",
            style = TextStyle(
                fontFamily = poppins_bold,
                fontSize = 30.sp,
                color = Color.Black
            )
        )

        // Text below the image
        Text(
            text = "Yuk, Kelola Keuangan Anda\nDengan Mudah!",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = arimo_reguler
            ),
            modifier = Modifier.padding(8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Button
        Button(
            onClick = { /* Handle button click */ },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            contentPadding = PaddingValues(),
            elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFC1CC), // Light pink
                                Color(0xFFE091A2)  // Darker pink
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mulai",
                    style = TextStyle(
                        fontFamily = poppins_bold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen1()
}