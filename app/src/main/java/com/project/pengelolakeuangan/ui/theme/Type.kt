package com.project.pengelolakeuangan.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.project.pengelolakeuangan.R

val poppins_black = FontFamily(
    Font(R.font.poppins_black)
)

val poppins_bold = FontFamily(
    Font(R.font.poppins_bold)
)

val poppins_reguler = FontFamily(
    Font(R.font.poppins_regular)
)

val arimo_bold = FontFamily(
    Font(R.font.arimo_bold)
)

val arimo_medium = FontFamily(
    Font(R.font.arimo_medium)
)

val arimo_reguler = FontFamily(
    Font(R.font.arimo_regular)
)




// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)