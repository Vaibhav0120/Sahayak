package com.example.thirdapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.example.thirdapplication.Repository.MainRepository

class MainViewModel(private val repository: MainRepository): ViewModel() {
    constructor() : this(MainRepository())

    fun loadData()=repository.items
    fun loadWeather()=repository.weather
}

