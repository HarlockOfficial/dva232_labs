package se.mdh.student.dva232.lab2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    val inputCurrency = ItemSelectionEvent(input = true, mainActivity = this)
    val outputCurrency = ItemSelectionEvent(input = false, mainActivity = this)
    lateinit var output: TextView
        private set
    lateinit var input: EditText
        private set
    private var answered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.user_input)
        output = findViewById(R.id.output)

        //ask for localization permissions
        checkPermissions()

        val spinner1: Spinner = findViewById(R.id.dropdown_input)

        GlobalScope.launch(Dispatchers.IO) {
            //get updated exchange rates
            ChangeRate.updateExchangeRates()
            var tmp: MutableList<String>? = ChangeRate.getCurrencyList()?.toMutableList()
            while (tmp == null) {
                delay(10)
                tmp = ChangeRate.getCurrencyList()?.toMutableList()
            }

            //add currencies to spinners
            GlobalScope.launch(Dispatchers.Main) {
                ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        tmp
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner1.adapter = adapter
                }
                spinner1.onItemSelectedListener = inputCurrency

                val spinner2: Spinner = findViewById(R.id.dropdown_output)
                ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        tmp
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner2.adapter = adapter
                }
                spinner2.onItemSelectedListener = outputCurrency

                //set listener for input
                input.addTextChangedListener(TextChangeEvent(mainActivity = this@MainActivity))

                //set listener for button
                findViewById<Button>(R.id.to_screen_two).setOnClickListener {
                    startActivity(Intent(this@MainActivity, ConversionRates::class.java).apply {
                        putExtra("se.mdh.student.dva232.lab2.Rates", tmp.toString())
                    })
                }
                findViewById<Button>(R.id.to_screen_three).setOnClickListener {
                    startActivity(Intent(this@MainActivity, Plot::class.java).apply {
                        putExtra("se.mdh.student.dva232.lab2.Rates", tmp.toString())
                    })
                }
            }

            //wait for permissions check
            while(!answered){
                delay(10)
            }

            //get location
            var provider: String? = null

            if(ActivityCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                provider = LocationManager.NETWORK_PROVIDER
            }
            if (ActivityCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                provider = LocationManager.GPS_PROVIDER
            }
            var actCurrency = "EUR"

            if(provider != null) {  //allowed to use geolocationw
                val locationManager: LocationManager =
                        getSystemService(LOCATION_SERVICE) as LocationManager
                var actualLocation: Location? = null
                //request single update deprecated in API 30
                locationManager.requestLocationUpdates(provider, 100L, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location == null) {
                            return
                        }
                        actualLocation = location
                        locationManager.removeUpdates(this)
                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
                    override fun onProviderEnabled(p0: String?) {}
                    override fun onProviderDisabled(p0: String?) {}
                }, Looper.getMainLooper())

                while (actualLocation == null) {
                    delay(10)
                }

                //get currency code
                actCurrency = Currency.getInstance(
                        Geocoder.getLocale(actualLocation!!)
                ).currencyCode
            }

            //change the input currency to country currency or EUR
            var eurIndex = -1
            for (i in 0 until spinner1.count) {
                if (spinner1.getItemAtPosition(i).toString() == "EUR") {
                    eurIndex = i
                }
                if (spinner1.getItemAtPosition(i).toString() == actCurrency) {
                    runOnUiThread {
                        spinner1.setSelection(i)
                    }
                    eurIndex = -1
                    break
                }
            }
            //valid currency not found
            if (eurIndex != -1) {
                runOnUiThread {
                    spinner1.setSelection(eurIndex)
                }
            }
        }
    }

    private fun checkPermissions(){
        var counter = 1 //number of permissions to ask
        val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if(counter--<=0){
                    answered = true
                    if (!isGranted) {
                        Toast.makeText(this@MainActivity, getString(R.string.location_disabled), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        //even if they're 2, they are counted as a single permission
        //both belong to geolocation
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

}