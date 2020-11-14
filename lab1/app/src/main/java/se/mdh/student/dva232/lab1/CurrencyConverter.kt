package se.mdh.student.dva232.lab1

import kotlin.math.round

class CurrencyConverter(private var amount: Double,private var inputCurrency: CurrencyType, private var outputCurrency: CurrencyType) {
    fun convert(): String {
        return ""+(round(amount * ChangeRate.getExchange(inputCurrency,outputCurrency)*1000.0)/1000.0)
    }
}