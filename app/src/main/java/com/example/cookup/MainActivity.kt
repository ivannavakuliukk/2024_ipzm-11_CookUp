package com.example.cookup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookup.ui.theme.CookUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookUpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InputScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InputScreen(modifier: Modifier = Modifier) {
    // Стан для зберігання тексту
    var inputText by remember { mutableStateOf("") }
    var displayedText by remember { mutableStateOf("") }

    // Розміщення елементів вертикально
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Текстовий елемент
        Text(
            text = "Введіть текст нижче",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Поле введення
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введіть щось") },
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопка
        Button(
            onClick = {
                // Встановлюємо текст для відображення при натисканні кнопки
                displayedText = inputText
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Натисніть")
        }

        // Відображення тексту в курсиві
        Text(
            text = displayedText,
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputScreenPreview() {
    CookUpTheme {
        InputScreen()
    }
}
