package se.mdh.student.dva232.lab2

class CurrencyConverter(private var amount: Double,private var inputCurrency: String, private var outputCurrency: String) {
    fun convert(): String {
        return "%,.2f".format(amount * ChangeRate.getExchange(inputCurrency,outputCurrency))
    }
}