package com.project.pengelolakeuangan.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.project.pengelolakeuangan.R
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen1(onNextClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.kucing), // Ganti dengan gambar Anda
            contentDescription = "Cat Image",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Halo, Jadikan Keuanganmu\nLebih Terkendali!",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNextClick) {
            Text("Yuk, Mulai Sekarang!")
        }
    }
}

@Composable
fun WelcomeScreen2(onFinishClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.kucingg), // Ganti dengan gambar Anda
            contentDescription = "Cat Image",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selamat Datang",
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Yuk, Kelola Keuangan Anda\nDengan Mudah!",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onFinishClick) {
            Text("Mulai")
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreens(onFinish: () -> Unit) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(count = 2, state = pagerState) { page ->  // Ganti 'count' menjadi 'pageCount'
        when (page) {
            0 -> WelcomeScreen1(
                onNextClick = {
                    coroutineScope.launch { pagerState.animateScrollToPage(1) }
                }
            )
            1 -> WelcomeScreen2(
                onFinishClick = { onFinish() }
            )
        }
    }
}
