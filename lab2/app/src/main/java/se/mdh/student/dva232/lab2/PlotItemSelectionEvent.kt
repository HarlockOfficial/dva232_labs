package se.mdh.student.dva232.lab2

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class PlotItemSelectionEvent(private val plotContext: Plot, private val input: Boolean): AdapterView.OnItemSelectedListener {
    public lateinit var selectedItem: String
        private set
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val data: JSONObject?= ChangeRate.getPlotData(plotContext.dateFrom.date, plotContext.dateTo.date)
            if(data==null){
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        plotContext,
                        plotContext.getString(R.string.get_plot_data_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                selectedItem = parent.getItemAtPosition(pos) as String

            }
            Log.e("Log00", data.toString())
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}