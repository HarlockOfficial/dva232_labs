package se.mdh.student.dva232.lab1

import android.text.Editable
import android.text.TextWatcher

class TextChangeEvent(private val mainActivity: MainActivity): TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if(editable.isNullOrBlank()){
            mainActivity.output.text = ""
            return
        }
        try{
            val value = editable.toString().toDouble()
            val inputCurrency: CurrencyType = mainActivity.inputCurrency.actCurrency
            val outputCurrency: CurrencyType = mainActivity.outputCurrency.actCurrency
            mainActivity.output.text = CurrencyConverter(value, inputCurrency, outputCurrency).convert()
        }catch (unused: NumberFormatException){
            return
        }
    }
}