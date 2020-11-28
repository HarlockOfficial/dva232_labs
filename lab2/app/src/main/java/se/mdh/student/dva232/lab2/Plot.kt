package se.mdh.student.dva232.lab2

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import com.androidplot.xy.XYPlot


//using https://github.com/halfhp/androidplot
//as plot library
class Plot: Activity() {
    public lateinit var plot: XYPlot
        private set
    public val itemStartSelectionEvent = PlotItemSelectionEvent(this, true)
    public val itemEndSelectionEvent = PlotItemSelectionEvent(this, false)
    public lateinit var dateFrom: CalendarView
        private set
    public lateinit var dateTo: CalendarView
        private set
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plot)

        val arr: String? = intent.getStringExtra("se.mdh.student.dva232.lab2.Rates")
        if (arr == null || arr.isEmpty()) { //should not happen, almost impossible, but IDK
            Toast.makeText(this, getText(R.string.resource_error), Toast.LENGTH_SHORT).show()
            this.onBackPressed()
            return
        }
        val array: List<String> = arr.substring(1, arr.length - 1).split(",")

        plot = findViewById(R.id.plot)

        //set default value to today
        dateFrom = findViewById(R.id.from)
        dateTo = findViewById(R.id.to)

        var spinner: Spinner = findViewById(R.id.start_currency)
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            array.toMutableList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = itemStartSelectionEvent

        spinner = findViewById(R.id.end_currency)
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            array.toMutableList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = itemEndSelectionEvent

    }
}