package se.mdh.student.dva232.lab2

import com.androidplot.xy.XYSeries
import java.text.SimpleDateFormat
import java.util.*

class PlotXYSeries(private val series: Map<String, Double>, private val title: String): XYSeries {
    override fun getTitle(): String {
        return title
    }

    override fun size(): Int {
        return series.size
    }

    override fun getX(index: Int): Number {
        val date: Date? = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(series.keys.elementAt(index))
        return date!!.time
    }

    override fun getY(index: Int): Number {
        //impossible to go out of bounds
        return series.getValue(series.keys.elementAt(index))
    }
}