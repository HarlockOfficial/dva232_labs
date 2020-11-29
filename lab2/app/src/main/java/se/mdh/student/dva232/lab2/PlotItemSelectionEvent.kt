package se.mdh.student.dva232.lab2

import android.view.View
import android.widget.AdapterView

class PlotItemSelectionEvent(private val plotContext: Plot, selected: String): AdapterView.OnItemSelectedListener {
    var selectedItem: String = selected
        private set
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        selectedItem = parent.getItemAtPosition(pos) as String
        plotContext.plot()
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {}
}