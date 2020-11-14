package se.mdh.student.dva232.lab1

class ChangeRate {
    //https://exchangeratesapi.io/
    //https://www.countryflags.io/
    companion object {
        val toNonCurrency: Map<CurrencyType, Double> = mapOf(
                CurrencyType.EUR to 1000.0,
                CurrencyType.USD to 1800.0,
                CurrencyType.CNY to 7820.0,
                CurrencyType.GBP to 900.0,
                CurrencyType.JPY to 123880.0,
                CurrencyType.KRW to 1311850.0,
                CurrencyType.SEK to 10280.0
        )

        fun getExchange(start: CurrencyType, end: CurrencyType): Double {
            val change1 = toNonCurrency[start] ?: return 0.0
            val change2 = toNonCurrency[end] ?: return 0.0
            return change2/change1
        }
    }
}