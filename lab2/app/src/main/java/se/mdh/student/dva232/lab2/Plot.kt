package se.mdh.student.dva232.lab2

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import com.androidplot.xy.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*


//using https://github.com/halfhp/androidplot
//as plot library
class Plot: Activity() {
    private lateinit var plot: XYPlot
    private lateinit var itemStartSelectionEvent: PlotItemSelectionEvent
    private lateinit var itemEndSelectionEvent: PlotItemSelectionEvent
    private lateinit var dateFromChangeListener: PlotDateChangeListener
    private lateinit var dateToChangeListener: PlotDateChangeListener

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
        val default: Int = intent.getIntExtra("se.mdh.student.dva232.lab2.Default", 0)

        plot = findViewById(R.id.plot)
        plot.layoutManager.remove(plot.legend)

        //adding listeners to get the selected day
        val dateFrom: CalendarView = findViewById(R.id.from)
        val dateTo: CalendarView = findViewById(R.id.to)
        val calendar = Calendar.getInstance()
        dateFromChangeListener = PlotDateChangeListener(this, "${calendar.get(Calendar.YEAR)-2}-${calendar.get(Calendar.MONTH)+1}-${calendar.get(Calendar.DAY_OF_MONTH)}")
        dateToChangeListener = PlotDateChangeListener(this, "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)+1}-${calendar.get(Calendar.DAY_OF_MONTH)}")
        dateFrom.setOnDateChangeListener(dateFromChangeListener)
        dateTo.setOnDateChangeListener(dateToChangeListener)

        var spinner: Spinner = findViewById(R.id.start_currency)
        ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                array.toMutableList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(default, false)
        itemStartSelectionEvent = PlotItemSelectionEvent(this, spinner.selectedItem.toString())
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
        spinner.setSelection(0, false)
        itemEndSelectionEvent = PlotItemSelectionEvent(this, spinner.selectedItem.toString())
        spinner.onItemSelectedListener = itemEndSelectionEvent

        plot()
    }
    
    fun plot() {
        GlobalScope.launch(Dispatchers.IO) {
            val data: JSONObject? = ChangeRate.getPlotData(
                    dateFromChangeListener.date,
                    dateToChangeListener.date
            )
            if (data == null || data.length() == 0) {
                runOnUiThread {
                    Toast.makeText(
                            this@Plot,
                            getString(R.string.get_plot_data_error),
                            Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            val currency: SortedMap<String, Double> = TreeMap()
            for(key in data.keys()){
                val obj = data.getJSONObject(key)
                val startValue = itemStartSelectionEvent.selectedItem.trim()
                val endValue = itemEndSelectionEvent.selectedItem.trim()
                val currency1: Double = if(startValue!="EUR") {
                    obj.getDouble(startValue)
                }else{
                    1.0
                }
                val currency2: Double = if(endValue!="EUR") {
                    obj.getDouble(endValue)
                }else{
                    1.0
                }
                currency[key] = (currency2/currency1)
            }

            //Starting plot stuff
            plot.clear()
            val series: XYSeries = PlotXYSeries(currency, "")
            val seriesFormat = LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null)
            plot.addSeries(series, seriesFormat)

            plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
                override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                    val tmp: String = (obj as Double).toString()
                    return StringBuffer("" + tmp[0]).
                            append(tmp.substring(2, 5)).append("-").
                            append(tmp.substring(5, 7)).append("-").
                            append(tmp.substring(7, 9))
                }
                override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                    return null
                }
            }
            plot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = object: Format(){
                override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                    return StringBuffer(String.format("%.3f", (obj as Double)))
                }
                override fun parseObject(source: String, pos: ParsePosition): Any? {
                    return null
                }
            }
            plot.redraw()
        }
    }
}