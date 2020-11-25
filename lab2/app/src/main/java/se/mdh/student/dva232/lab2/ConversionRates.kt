package se.mdh.student.dva232.lab2

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*

class ConversionRates: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rates_conversion)

        val arr:String? = intent.getStringExtra("se.mdh.student.dva232.lab2.Rates")
        if(arr==null || arr.isEmpty()) { //should not happen, almost impossible, but IDK
            Toast.makeText(this, getText(R.string.resource_error), Toast.LENGTH_SHORT).show()
            this.onBackPressed()
            return
        }
        val array: List<String> = arr.substring(1,arr.length-1).split(",")
        val currencyTable: TableLayout = findViewById(R.id.currency_grid)
        val spinner: Spinner = findViewById(R.id.currency_selector)

        ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            array.toMutableList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                currencyTable.removeAllViewsInLayout()
                val startCurrency: String = parent.getItemAtPosition(pos).toString()
                for(currency:String in array){
                    if(currency == startCurrency)
                        continue
                    val row:View = layoutInflater.inflate(R.layout.conversion_table_row,
                                    currencyTable,false)
                    (row.findViewById(R.id.currency_code) as TextView).text = currency
                    (row.findViewById(R.id.currency_change) as TextView).text = "%,.5f".format(
                            ChangeRate.getExchange(startCurrency, currency))
                    currencyTable.addView(row)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }


    }
}