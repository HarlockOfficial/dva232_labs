package se.mdh.student.dva232.lab1

import kotlin.math.round

class CurrencyConverter(private var amount: Double,private var inputCurrency: CurrencyType, private var outputCurrency: CurrencyType) {
    fun convert(): String {
        return "%,.2f".format(amount * ChangeRate.getExchange(inputCurrency,outputCurrency))
    }
}