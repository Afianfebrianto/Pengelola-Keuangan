package com.project.pengelolakeuangan.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.pengelolakeuangan.R
import com.project.pengelolakeuangan.utils.poppinsFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    var displayedText by remember { mutableStateOf("") }
    val fullText = "Pengelola Keuangan" // Teks yang ingin ditampilkan
    val animationDuration = 50L // Durasi animasi per huruf (dalam milidetik)

    // Animasi munculnya huruf satu per satu
    LaunchedEffect(fullText) {
        fullText.forEachIndexed { index, _ ->
            displayedText = fullText.substring(0, index + 1)
            delay(animationDuration)
        }
        delay(1000) // Tambahkan jeda setelah animasi selesai
        onFinish()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyLottie()
            Text(
                text = displayedText, // Teks dinamis yang muncul satu per satu
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = poppinsFamily,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun MyLottie() {
    val preLoaderLottie by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animatelogo)
    )

    val preLoaderProgress by animateLottieCompositionAsState(preLoaderLottie,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
    )

    LottieAnimation(
        composition = preLoaderLottie,
        progress = {preLoaderProgress},
//        modifier = Modifier.fillMaxSize()
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SplashScreen {}
}
