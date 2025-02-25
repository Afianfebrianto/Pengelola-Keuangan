package com.project.pengelolakeuangan.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.ui.component.WelcomeButton
import com.project.pengelolakeuangan.utils.poppinsFamily
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
        AnimateManageMoney()

        Text(
            text = "Halo, Jadikan Keuanganmu\nLebih Terkendali!",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            fontFamily = poppinsFamily
        )

        Spacer(modifier = Modifier.height(32.dp))

        WelcomeButton(
            text = "Yuk, Mulai Sekarang!",
            onClick = onNextClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
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
        AnimateDompet()

        Text(
            text = "Selamat Datang",
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black),
            fontFamily = poppinsFamily
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Yuk, Kelola Keuangan Anda\nDengan Mudah!",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
            fontFamily = poppinsFamily,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        WelcomeButton(
            text = "Mulai",
            onClick = onFinishClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AnimateDompet() {
    val preLoaderLottie by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animation_dompet)
    )

    val preLoaderProgress by animateLottieCompositionAsState(preLoaderLottie,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
    )

    LottieAnimation(
        composition = preLoaderLottie,
        progress = {preLoaderProgress},
        modifier = Modifier.size(150.dp)
    )

}

@Composable
fun AnimateManageMoney() {
    val preLoaderLottie by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animation_manage_money)
    )

    val preLoaderProgress by animateLottieCompositionAsState(preLoaderLottie,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
    )

    LottieAnimation(
        composition = preLoaderLottie,
        progress = {preLoaderProgress},
        modifier = Modifier.size(300.dp)
    )

}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreens(onFinish: () -> Unit) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(count = 2, state = pagerState) {
        when (it) {
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
