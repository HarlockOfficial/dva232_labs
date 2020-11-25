package se.mdh.student.dva232.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val inputCurrency = ItemSelectionEvent(input = true, mainActivity = this)
    val outputCurrency = ItemSelectionEvent(input = false, mainActivity = this)

    lateinit var output: TextView
        private set
    lateinit var input: EditText
        private set
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.user_input)
        output = findViewById(R.id.output)

        GlobalScope.launch {
            ChangeRate.updateExchangeRates()
            var tmp : MutableList<String>? = ChangeRate.getCurrencyList()?.toMutableList()
            while (tmp==null){
                delay(10)
                tmp = ChangeRate.getCurrencyList()?.toMutableList()
            }

            //add currencies to spinners
            GlobalScope.launch(Dispatchers.Main) {
                var spinner: Spinner = findViewById(R.id.dropdown_input)
                ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    tmp
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
                spinner.onItemSelectedListener = inputCurrency

                spinner = findViewById(R.id.dropdown_output)

                ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    tmp
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }

                spinner.onItemSelectedListener = outputCurrency
                input.addTextChangedListener(TextChangeEvent(mainActivity = this@MainActivity))
                findViewById<Button>(R.id.to_screen_two).setOnClickListener {
                    startActivity(Intent(this@MainActivity, ConversionRates::class.java).apply {
                        putExtra("se.mdh.student.dva232.lab2.Rates", tmp.toString())
                    })
                }
            }
        }
    }
}