package se.mdh.student.dva232.lab2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
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
                    currencyExchangeRates[key] = tmp.getJSONObject("rates").getDouble(key)
                }
                currencyExchangeRates["EUR"]=1.0
            }catch(unused: JSONException){}
        }
        fun getCurrencyList(): MutableSet<String>? {
            return if(currencyExchangeRates.keys.size>0) currencyExchangeRates.keys else null
        }
        fun getExchange(start: String, end: String): Double {
            if(currencyExchangeRates.isEmpty()){
                return 0.0
            }
            val change1:Double = currencyExchangeRates[start] ?: return 0.0
            val change2:Double = currencyExchangeRates[end] ?: return 0.0   //always null for no reason
            return change2/change1
        }
    }
}