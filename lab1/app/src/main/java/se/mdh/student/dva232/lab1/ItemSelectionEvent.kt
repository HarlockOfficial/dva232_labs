package se.mdh.student.dva232.lab1

import android.view.View
import android.widget.AdapterView

class ItemSelectionEvent(private val input: Boolean, private val mainActivity: MainActivity): AdapterView.OnItemSelectedListener {
    lateinit var actCurrency: CurrencyType
        private set

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val item : String = parent.getItemAtPosition(pos) as String
        if(input) {
            actCurrency = CurrencyType.valueOf(item)
        }else {
            actCurrency = CurrencyType.valueOf(item)
            mainActivity.output.text = ""
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}