package se.mdh.student.dva232.lab2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private var useGeolocation : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ask for permissions
        check_permissions()

        input = findViewById(R.id.user_input)
        output = findViewById(R.id.output)

        GlobalScope.launch(Dispatchers.IO) {
            ChangeRate.updateExchangeRates()
            var tmp : MutableList<String>? = ChangeRate.getCurrencyList()?.toMutableList()
            while (tmp==null) {
                delay(10)
                tmp = ChangeRate.getCurrencyList()?.toMutableList()
            }

            //add currencies to spinners
            GlobalScope.launch(Dispatchers.Main) {
                var spinner: Spinner = findViewById(R.id.dropdown_input)
                ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    tmp
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
                spinner.onItemSelectedListener = inputCurrency

                if (ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val locationManager: LocationManager =
                        getSystemService(LOCATION_SERVICE) as LocationManager
                    var provider: String = LocationManager.NETWORK_PROVIDER
                    if (useGeolocation) {
                        provider = LocationManager.GPS_PROVIDER
                    }
                    val location = locationManager.getLastKnownLocation(provider)
                    val geoCoder = Geocoder(this@MainActivity)
                    if (location != null) {
                        //TODO buggy need to ask (does not work on virtual device)
                        val actCurrency = Currency.getInstance(
                            geoCoder.getFromLocation(
                                location.latitude, location.longitude,
                                1
                            )[0].locale
                        ).currencyCode
                        var eurIndex = -1
                        for (i in 0 until spinner.count) {
                            if(spinner.getItemAtPosition(i).toString() == "EUR"){
                                eurIndex = i
                            }
                            if (spinner.getItemAtPosition(i).toString() == actCurrency) {
                                spinner.setSelection(i)
                                eurIndex = -1
                                break
                            }
                        }
                        if(eurIndex != -1){
                            spinner.setSelection(eurIndex)
                        }
                    }
                }

                spinner = findViewById(R.id.dropdown_output)
                ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    tmp
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }

                spinner.onItemSelectedListener = outputCurrency
                input.addTextChangedListener(TextChangeEvent(mainActivity = this@MainActivity))
                findViewById<Button>(R.id.to_screen_two).setOnClickListener {
                    startActivity(Intent(this@MainActivity, ConversionRates::class.java).apply {
                        putExtra("se.mdh.student.dva232.lab2.Rates", tmp.toString())
                    })
                }
            }
        }
    }

    private fun check_permissions(){
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (!isGranted) {
                    useGeolocation = true
                }else{
                    Toast.makeText(this, getString(R.string.location_disabled), Toast.LENGTH_SHORT).show()
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            useGeolocation = true
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

}