package se.mdh.student.dva232.lab2

import android.net.UrlQuerySanitizer
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


class ChangeRate: ViewModel(){
    //https://www.countryflags.io/
    companion object {
        private var currencyExchangeRates: HashMap<String, Double> = HashMap()
        suspend fun updateExchangeRates() = withContext(Dispatchers.IO){
            try {
                val tmp = JSONObject(URL("https://api.exchangeratesapi.io/latest").readText())
                for(key:String in tmp.getJSONObject("rates").keys()){
                    currencyExchangeRates[key.trim()] = tmp.getJSONObject("rates").getDouble(key)
                }
                currencyExchangeRates["EUR"]=1.0
            }catch(unused: JSONException){}
        }
        fun getCurrencyList(): MutableSet<String>? {
            return if(currencyExchangeRates.keys.size>0) currencyExchangeRates.keys.toSortedSet() else null
        }
        fun getExchange(start: String, end: String): Double {
            if(currencyExchangeRates.isEmpty()){
                GlobalScope.launch (Dispatchers.IO){
                    updateExchangeRates()
                }
                return 0.0
            }
            val change1:Double = currencyExchangeRates[start.trim()] ?: return 0.0
            val change2:Double = currencyExchangeRates[end.trim()] ?: return 0.0
            return change2/change1
        }
        fun getPlotData(dateFrom: Long, dateTo: Long): JSONObject?{
            //not working!!!
            val calendar = Calendar.getInstance()
            //start date
            calendar.timeInMillis = dateFrom
            val yyFrom = calendar.get(Calendar.YEAR)
            val mmFrom = calendar.get(Calendar.MONTH)+1
            val ddFrom = calendar.get(Calendar.DAY_OF_MONTH)
            //finish date
            calendar.timeInMillis = dateTo
            val yyTo = calendar.get(Calendar.YEAR)
            val mmTo = calendar.get(Calendar.MONTH)+1
            val ddTo = calendar.get(Calendar.DAY_OF_MONTH)
            return try {
                val x = JSONObject(
                    URL(
                        "https://api.exchangeratesapi.io/history?start_at=" +
                                "$yyFrom-$mmFrom-$ddFrom&end_at=$yyTo-$mmTo-$ddTo"
                    ).readText()
                )
                Log.e("Log000645", x.toString())
                x.getJSONObject("rates")
            }catch (unused: JSONException){
                null
            }catch (unused: MalformedURLException){
                null
            }
        }
    }
}