package com.endurancetech.unitconverterapp

import kotlinx.serialization.Serializable
import java.text.DecimalFormat


@Serializable
data class Conversion(
    val inputValue: Double, val fromUnit: String, val convertedValue: Double, val toUnit: String
) {
    fun getFormattedConversion(): String {
        val df = DecimalFormat("#.##")
        return "$inputValue $fromUnit = ${df.format(convertedValue)} $toUnit"
    }
}

