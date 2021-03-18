package com.hr.musalatask.views.weatherMainScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.hr.musalatask.R
import com.hr.musalatask.databinding.ActivityMainBinding
import com.hr.musalatask.internet.NetworkState
import com.hr.musalatask.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<WeatherViewModel>()
    lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bind()
        observeLiveData()
        getLocation()

    }

    fun bind() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getDataFromServer(query, null, null)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        binding.myLocation.setOnClickListener {
            getLocation()
        }
        binding.apply {
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }
    }

    fun observeLiveData() {
        viewModel.networkState.observe(this@MainActivity, Observer { it ->
            if (it != null) {
                when (it.status) {
                    NetworkState.Status.RUNNING -> {
                        ProgressLoading.show(this)
                        binding.noData.visibility = View.GONE
                    } // LOADING
                    NetworkState.Status.SUCCESS -> {
                        ProgressLoading.dismiss()
                        binding.noData.visibility = View.GONE
                    }// LOADED
                    NetworkState.Status.FAILED -> {
                        ProgressLoading.dismiss()
                        CustomToast.showErrorMessage(this, it.msg as String)
                        binding.noData.visibility = View.VISIBLE

                    } // FAILED
                }
                viewModel._networkState.value = null
            }
        })

        viewModel.weatherData.observe(this@MainActivity, Observer { weatherResponseModel ->
            binding.humidity.text = weatherResponseModel.main.humidity.toString() + "%"
            if (weatherResponseModel.weather.isNotEmpty()) {
                binding.weatherDescription.text = weatherResponseModel.weather[0].description
                binding.weatherIcon.visibility = View.VISIBLE
                binding.weatherIcon.loadImage(
                    "http://openweathermap.org/img/wn/${weatherResponseModel.weather[0].icon}@2x.png",
                    getProcessDrawable(binding.weatherIcon.context)
                )
            } else {
                binding.weatherDescription.text = ""
                binding.weatherIcon.visibility = View.INVISIBLE
            }
            binding.temp.text =
                "${weatherResponseModel.main.temp.roundToInt()}\u00B0  "//for Fahrenheit  u2109 for celiouc u2103
            binding.wind.text = "${weatherResponseModel.wind.speed} kmph"
            binding.windDirection.WindowDirectionFromDegree(weatherResponseModel.wind.deg)
            //presure mb
            binding.pressure.text = weatherResponseModel.main.pressure.toString() + " mb"
            binding.feelsLike.text =
                weatherResponseModel.main.feelsLike.roundToInt().toString() + " °"
            binding.minMax.text =
                "${weatherResponseModel.main.tempMin.roundToInt()}° / ${weatherResponseModel.main.tempMax.roundToInt()}°"
            binding.Country.text =
                "${weatherResponseModel.name},${weatherResponseModel.sys.country}"
            binding.sunRise.setTimeFromDateTimeStamp(weatherResponseModel.sys.sunrise.toLong())
            binding.sunSet.setTimeFromDateTimeStamp(weatherResponseModel.sys.sunset.toLong())
            binding.searchView.setQuery(weatherResponseModel.name, false)

        })
    }

    fun getLocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val location: Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        if (location != null && location.time > Calendar.getInstance()
                .timeInMillis - 5 * 60 * 1000
        ) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
            viewModel.getDataFromServer(
                null,
                location.latitude.toString(),
                location.longitude.toString()
            )
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> showPermissionRequestExplanation(
                    getString(R.string.location),
                    getString(R.string.needOfPerimission)
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                    )
                }
                //Tell to user the need of grant permission
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    override fun onLocationChanged(location: Location) {
        Log.v("Location Changed", "${location.latitude} and  ${location.longitude}")
        locationManager.removeUpdates(this)
    }

}