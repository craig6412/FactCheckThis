package com.example.factcheckthis

import android.Manifest
import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.factcheckthis.ui.theme.FactCheckThisTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FactCheckThisTheme(darkTheme = true) {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val weatherState: MutableState<WeatherInfo?> = remember { mutableStateOf(null) }
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    scope.launch {
                        weatherState.value = WeatherService.fetchWeather(location.latitude, location.longitude)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fact Check This", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: open menu */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { /* TODO: change categories */ }, modifier = Modifier.padding(top = 16.dp)) {
                Text("Change Categories")
            }

            AndroidView(
                factory = { ctx ->
                    CalendarView(ctx).apply { date = System.currentTimeMillis() }
                },
                modifier = Modifier.padding(top = 16.dp)
            )

            val info = weatherState.value
            if (info != null) {
                Text(
                    text = "High ${info.maxTemp}°  Low ${info.minTemp}°  ${info.description}",
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                Text(
                    text = "Loading weather...",
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

