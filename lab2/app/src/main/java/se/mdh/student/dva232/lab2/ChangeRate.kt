package se.mdh.student.dva232.lab2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


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
    }
}