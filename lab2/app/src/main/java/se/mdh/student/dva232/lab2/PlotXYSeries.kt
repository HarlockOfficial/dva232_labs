package se.mdh.student.dva232.lab2

import android.util.Log
import com.androidplot.xy.XYSeries

class PlotXYSeries(private val series: Map<String, Double>, private val title: String): XYSeries {
    override fun getTitle(): String {
        return title
    }

    override fun size(): Int {
        return series.size
    }

    override fun getX(index: Int): Number {
        val date: List<String> = series.keys.elementAt(index).split("-")
        return (date[0]+date[1]+date[2]).toInt()
    }

    override fun getY(index: Int): Number {
        //impossible to go out of bounds
        return series.getValue(series.keys.elementAt(index))
    }
}