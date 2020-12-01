package se.mdh.student.dva232.lab2

import android.widget.CalendarView

class PlotDateChangeListener(private val plotContext: Plot, today: String): CalendarView.OnDateChangeListener {
    var date: String = today
        private set
    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, day: Int) {
        date = "$year-${month+1}-$day"
        plotContext.plot()
    }
}