package se.mdh.student.dva232.lab1

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlin.math.round

class ConversionRates: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rates_conversion)

        val arr:Array<String>? = intent.getStringArrayExtra("se.mdh.student.dva232.lab1.Rates")
        if(arr==null || arr.isEmpty()) { //should not happen, almost impossible, but IDK
            Toast.makeText(this, getText(R.string.resource_error), Toast.LENGTH_SHORT).show()
            this.onBackPressed()
            return
        }
        val currencyTable: TableLayout = findViewById(R.id.currency_grid)
        val spinner: Spinner = findViewById(R.id.currency_selector)
        ArrayAdapter.createFromResource(this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                currencyTable.removeAllViewsInLayout()

                val startCurrency: CurrencyType = CurrencyType.valueOf(parent.getItemAtPosition(pos).toString())
                for(currency:String in arr){
                    if(currency == startCurrency.toString())
                        continue
                    val row:View = layoutInflater.inflate(R.layout.conversion_table_row, currencyTable,false)
                    (row.findViewById(R.id.currency_code) as TextView).text = currency
                    (row.findViewById(R.id.currency_change) as TextView).text = "%,.5f".format(
                            ChangeRate.getExchange(startCurrency, CurrencyType.valueOf(currency)))
                    currencyTable.addView(row)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }
}