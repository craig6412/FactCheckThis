package com.example.factcheckthis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.factcheckthis.ui.theme.FactCheckThisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FactCheckThisTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FactCheckThisTheme {
        Greeting("Android")
    }
}
@Composable
fun MainScreen(
    isFirstLaunch: Boolean,
    onCategoriesSelected: (List<String>) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fact Check This", color = Color.White) },
                backgroundColor = Color.Black
            )
        },
        backgroundColor = Color(0xFF121212) // dark background
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            if (isFirstLaunch) {
                CategorySelectionScreen(onCategoriesSelected)
            } else {
                FactOfTheDayScreen()
            }
        }
    }
}
