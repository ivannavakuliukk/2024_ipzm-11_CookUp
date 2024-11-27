package com.example.cookup.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cookup.R



private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Pink,
    tertiary = Red,
    onPrimary = Color.Black

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val Poppins = FontFamily(
    Font(R.font.poppins_regular)
)
val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Poppins,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Poppins,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    labelLarge =  TextStyle(
        fontFamily = Poppins,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.15.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Poppins,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.15.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Poppins,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.15.sp
    ),
    displaySmall =  TextStyle(
        fontFamily = Poppins,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun CookUpTheme(
    content: @Composable () -> Unit
) {
    // Використовуємо лише світлу кольорову схему
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}
