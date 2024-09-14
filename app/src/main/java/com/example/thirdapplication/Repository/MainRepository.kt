package com.example.thirdapplication.Repository

import com.example.thirdapplication.Domain.ListDomain
import com.example.thirdapplication.Domain.WeatherDomain

class MainRepository {
    val items = mutableListOf(
        ListDomain("Predictions", "Highly Accurate Predictions with AI", "prediction", "Clear"),
        ListDomain("Live Weather", "min temp: 26°c, max temp 31°c", "live_weather", "Sunny"),
        ListDomain("Emergency Contacts", "Add Emergency Contacts", "emergency_contacts", "Setup Now"),
        ListDomain("Check Events", "Check Awareness Events Info", "campaign", "Available"),
        ListDomain("Do's / Don't", "A Simple Choice Can Save Life", "dos_donts", "Check Now"),
        ListDomain("Personal Info", "Save Personal Info For Emergencies", "personal_info", "Setup Now"),
        //Not Visible Last one
        ListDomain("Emergency Contacts", "Save your Emergency Contact Info", "ic_contacts", "Setup Now"),
    )

    val weather = mutableListOf(
        WeatherDomain("Temperature","26°c | Mostly Cloudy","temp"),
        WeatherDomain("Humidity","73% The dew point is 28°C","humidity"),
        WeatherDomain("Precipitation","8mm Today","rain"),
        WeatherDomain("Wind Speed","Speed: 4kph Direction:41°NE","wind"),
        WeatherDomain("Air Quality","112 Moderately Polluted","air_quality_index"),
        WeatherDomain("UV Index","6 (High) Use sun protection until 4 PM","uv_index"),
    )
}