package com.example.factcheckthis

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class WeatherInfo(val maxTemp: Double, val minTemp: Double, val description: String)

object WeatherService {
    private val client = OkHttpClient()

    suspend fun fetchWeather(lat: Double, lon: Double): WeatherInfo? = withContext(Dispatchers.IO) {
        val url = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=auto"
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@withContext null
            val body = response.body?.string() ?: return@withContext null
            val json = JSONObject(body)
            val daily = json.getJSONObject("daily")
            val max = daily.getJSONArray("temperature_2m_max").getDouble(0)
            val min = daily.getJSONArray("temperature_2m_min").getDouble(0)
            val code = daily.getJSONArray("weathercode").getInt(0)
            val desc = weatherCodeToDescription(code)
            WeatherInfo(max, min, desc)
        }
    }

    private fun weatherCodeToDescription(code: Int): String = when (code) {
        0 -> "Clear"
        1, 2 -> "Partly Cloudy"
        3 -> "Cloudy"
        in 45..48 -> "Fog"
        in 51..67 -> "Drizzle"
        in 71..75 -> "Snow"
        in 80..82 -> "Rain Showers"
        in 95..99 -> "Thunderstorm"
        else -> "Unknown"
    }
}

