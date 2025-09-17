package com.example.mylastapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mylastapp.R

// Set of Material typography styles to start with

val balooFontType = FontFamily(
    Font(
        R.font.baloo_chettan_regular
    )
)

val interFontType = FontFamily(
    Font(
        R.font.inter_bold,
        FontWeight.Bold
    )
)

val arimaFontType = FontFamily(
    Font(
        R.font.arima_madurai_black,
        FontWeight.Black
    ),
    Font(
        R.font.arima_madurai_bold,
        FontWeight.Bold
    ),
    Font(
        R.font.arima_madurai_medium,
        FontWeight.Medium
    )
)

val BalooTypography = Typography(
    titleMedium = TextStyle(
        fontFamily = balooFontType,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp
    ),
    displayMedium = TextStyle(
        fontFamily = balooFontType,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    )
)

val ArimaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = arimaFontType,
        fontWeight = FontWeight.Black,
        fontSize = 25.sp
    ),
    displayMedium = TextStyle(
        fontFamily = arimaFontType,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp
    ),
    displaySmall = TextStyle(
        fontFamily = arimaFontType,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

)

val InterTypography = Typography(
    titleMedium = TextStyle(
        fontFamily = interFontType,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    )
)

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