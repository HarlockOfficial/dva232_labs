package se.mdh.student.dva232.lab1

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class ConversionRates: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rates_conversion)
        var str = intent.getStringExtra("se.mdh.student.dva232.lab1.Rates")
        if(!str.isNullOrBlank()) {
            str = str.substring(1,str.length-1)
            val toNonCurrency: Map<CurrencyType, Double> = str.split(",").associate {
                val (left, right) = it.split("=")
                CurrencyType.valueOf(left.trim()) to right.trim().toDouble()
            }
            Log.e("Log00", toNonCurrency.toString())
        } else
            Toast.makeText(this, getText(R.string.resource_error), Toast.LENGTH_SHORT).show()
    }
}