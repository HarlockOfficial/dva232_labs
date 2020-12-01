package se.mdh.student.dva232.lab2

import android.view.View
import android.widget.AdapterView
import java.lang.NumberFormatException

class ItemSelectionEvent(private val input: Boolean, private val mainActivity: MainActivity): AdapterView.OnItemSelectedListener {
    lateinit var actCurrency: String
        private set

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        try{
            val inputCurrency: String
            val outputCurrency: String
            val item : String = parent.getItemAtPosition(pos) as String
            if(input) {
                actCurrency = item
                inputCurrency = actCurrency
                outputCurrency = mainActivity.outputCurrency.actCurrency
            }else {
                actCurrency = item
                inputCurrency = mainActivity.inputCurrency.actCurrency
                outputCurrency = actCurrency
            }
            val value: Double = mainActivity.input.text.toString().toDouble()
            mainActivity.output.text = CurrencyConverter(value, inputCurrency , outputCurrency).convert()
        }catch (unused: NumberFormatException) {
            return
        }catch (unused: UninitializedPropertyAccessException){
            return
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}