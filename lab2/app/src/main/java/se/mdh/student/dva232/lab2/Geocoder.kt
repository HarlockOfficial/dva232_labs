package se.mdh.student.dva232.lab2

import android.location.Location
import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*

//not all manufacturers implements Geocoder
//better to implement the needed feature
class Geocoder: ViewModel(){
    companion object {
        fun getLocale(location: Location): Locale {
            return try {
                val json = JSONObject(URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=" +
                        location.latitude + "&lon=" +
                        location.longitude + "&zoom=18").readText())
                Locale.getAvailableLocales().first {
                    it.country == json.getJSONObject("address").
                        getString("country_code").toUpperCase(Locale.ROOT)
                }
            } catch (unused: NoSuchElementException) {
                Locale.GERMANY
            } catch (unused: JSONException) {
                Locale.GERMANY
            }
        }
    }
}