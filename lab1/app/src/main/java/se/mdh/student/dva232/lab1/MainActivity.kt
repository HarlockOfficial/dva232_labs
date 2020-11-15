package se.mdh.student.dva232.lab1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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

        //add currencies to spinners
        var spinner: Spinner = findViewById(R.id.dropdown_input)
        ArrayAdapter.createFromResource(this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = inputCurrency

        spinner = findViewById(R.id.dropdown_output)
        ArrayAdapter.createFromResource(this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = outputCurrency

        input.addTextChangedListener(TextChangeEvent(mainActivity = this))
        findViewById<Button>(R.id.to_screen_two).setOnClickListener {
            startActivity(Intent(this.baseContext, ConversionRates::class.java).apply {
                putExtra("se.mdh.student.dva232.lab1.Rates", (resources.getStringArray(R.array.currencies) as Array<String>))
            })
        }
    }
}