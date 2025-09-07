package com.example.factcheckthis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.factcheckthis.ui.theme.FactCheckThisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedCategories by rememberSaveable { mutableStateOf<List<String>?>(null) }

            FactCheckThisTheme(darkTheme = true) {
                MainScreen(
                    isFirstLaunch = selectedCategories == null,
                    onCategoriesSelected = { selectedCategories = it }
                )
            }
        }
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
                title = { Text("Fact Check This") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isFirstLaunch) {
                CategorySelectionScreen(onCategoriesSelected)
            } else {
                FactOfTheDayScreen()
            }
        }
    }
}

@Composable
fun CategorySelectionScreen(onCategoriesSelected: (List<String>) -> Unit) {
    val categories = listOf(
        "Science" to "🔬",
        "Space" to "🚀",
        "History" to "📜",
        "Sports" to "🏅",
        "Animals" to "🐾",
        "Hunting" to "🏹",
        "Racing" to "🏁",
        "Weird" to "🌀"
    )
    var selected by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose 3 categories", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(categories) { (name, emoji) ->
                val isSelected = name in selected
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selected = if (isSelected) {
                            selected - name
                        } else {
                            if (selected.size < 3) selected + name else selected
                        }
                    },
                    label = { Text("$emoji $name") },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.DarkGray,
                        labelColor = Color.White,
                        selectedContainerColor = Color.Gray,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(0.8f)
                )
            }
        }
        Button(
            onClick = { onCategoriesSelected(selected.toList()) },
            enabled = selected.size == 3,
            shape = RoundedCornerShape(50)
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun FactOfTheDayScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Fact of the Day", color = Color.White)
    }
}

