package com.endurancetech.unitconverterapp

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val lastConversionRepository = LastConversionRepository(application)

    var inputValue by mutableStateOf("")
        private set

    var fromUnit by mutableStateOf("Meters")
        private set

    var toUnit by mutableStateOf("Feet")
        private set

    var result by mutableStateOf("")
        private set

    val lastConversion = lastConversionRepository.lastConversionFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val units = listOf("Meters", "Feet", "Inches", "Centimeters")

    fun onInputValueChange(value: String) {
        inputValue = value
    }

    fun onFromUnitChange(unit: String) {
        fromUnit = unit
    }

    fun onToUnitChange(unit: String) {
        toUnit = unit
    }

    fun convert() {
        if (inputValue.isBlank()) {
            Toast.makeText(getApplication(), "Please enter a value to convert! 📝", Toast.LENGTH_SHORT).show()
            return
        }

        val value = inputValue.toDoubleOrNull()
        if (value == null) {
            Toast.makeText(getApplication(), "Please enter a valid number! 🔢", Toast.LENGTH_SHORT).show()
            return
        }

        val conversionRate = getConversionRate(fromUnit, toUnit)
        val convertedValue = value * conversionRate
        val df = java.text.DecimalFormat("#.##")

        result = "${df.format(convertedValue)} $toUnit"

        val newConversion = Conversion(value, fromUnit, convertedValue, toUnit)

        viewModelScope.launch {
            lastConversionRepository.saveLastConversion(newConversion)
        }
    }

    private fun getConversionRate(from: String, to: String): Double {
        val meterConversion = mapOf(
            "Meters" to 1.0, "Feet" to 0.3048, "Inches" to 0.0254, "Centimeters" to 0.01
        )
        return meterConversion[from]!! / meterConversion[to]!!
    }
}