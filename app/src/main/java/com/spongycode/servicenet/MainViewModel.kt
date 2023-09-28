package com.spongycode.servicenet

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.servicenet.data.ApiService
import com.spongycode.servicenet.data.model.University
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _universitiesList =
        MutableStateFlow(listOf(University(name = "Fetching Universities...")))
    val universitiesList = _universitiesList.asStateFlow()


    private val _time = MutableStateFlow("Getting Current Time...")
    val time = _time.asStateFlow()

    private val _quote = MutableStateFlow("Loading Random Quotes...")
    val quote = _quote.asStateFlow()

    private val _foregroundServiceStarted = MutableStateFlow(false)
    val foregroundServiceStarted = _foregroundServiceStarted.asStateFlow()

    val options = listOf("Universities List", "Random Quotes", "Current Time")
    private val _selectedOptionText = MutableStateFlow(options[0])
    val selectedOptionText = _selectedOptionText.asStateFlow()
    private val countries = listOf(
        "Canada", "Australia", "Germany",
        "France", "Japan", "Brazil", "India", "China",
        "Mexico", "Argentina", "Italy", "Spain", "Egypt",
        "Nigeria", "Kenya", "Turkey", "Indonesia", "Thailand", "Malaysia"
    )

    private val colors = listOf(
        Color(0xFFD3D3D3), Color(0xFFD3F1F1), Color(0xFFE6E6FA), Color(0xFFD2F7E4),
        Color(0xFFF0FFF0), Color(0xFFFFFFF0), Color(0xFFFDF5E6), Color(0xFFE0FFFF),
        Color(0xFFF5F5DC), Color(0xFFF0F8FF), Color(0xFFEEE3E5), Color(0xFFC6D4DD),
        Color(0xFFFFE4E1), Color(0xFFDEF3DE), Color(0xFFE2E1D2), Color(0xFFF0DDD6),
        Color(0xFFF7E4E4), Color(0xFFFAF7DC), Color(0xFFDFF8F8), Color(0xFFE3F8F7),
        Color(0xFFD0D9E4), Color(0xFFF1F1DB), Color(0xFFD2DEEB), Color(0xFFEBD9EB),
        Color(0xFFA3A0B4)
    )
    private val _backgroundColor = MutableStateFlow<Color>(colors.random())
    val backgroundColor = _backgroundColor.asStateFlow()

    fun universalSearch() {
        when (selectedOptionText.value) {
            options[0] -> getUniversitiesList()
            options[1] -> getRandomQuote()
            options[2] -> getCurrentTime()
        }
    }

    private fun getUniversitiesList() {
        viewModelScope.launch {
            val universitiesList =
                ApiService.create(ApiService.BaseUrls.BASE_URL_UNIVERSITY.toString())
                    .getUniversitiesList(countries.random())
            _universitiesList.value = universitiesList
            changeBackgroundColor()
        }
    }

    private fun getRandomQuote() {
        viewModelScope
            .launch {
                val quote = ApiService.create(ApiService.BaseUrls.BASE_URL_QUOTE.toString())
                    .getRandomQuote()
                val quoteString = quote.content
                _quote.value = quoteString
                changeBackgroundColor()
            }
    }

    private fun getCurrentTime() {
        viewModelScope
            .launch {
                val currentTime =
                    ApiService.create(ApiService.BaseUrls.BASE_URL_TIME.toString())
                        .getCurrentTime()
                val datetimeString = currentTime.datetime
                _time.value = datetimeString
                changeBackgroundColor()
            }
    }

    fun changeSelectedOptionText(selectedOptionText: String) {
        _selectedOptionText.value = selectedOptionText
    }

    fun startForegroundService() {
        _foregroundServiceStarted.value = true
    }

    private fun changeBackgroundColor() {
        _backgroundColor.value = colors.random()
    }

}


