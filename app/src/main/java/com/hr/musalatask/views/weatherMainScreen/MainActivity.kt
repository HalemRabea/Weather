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
import com.hr.musalatask.model.WeatherResponseModel
import com.hr.musalatask.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,LocationListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<WeatherViewModel>()
    lateinit var locationManager:LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        observeLiveData()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getDataFromServer(query, null, null)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        getLocation()
        binding.myLocation.setOnClickListener{
            getLocation()
        }
    }

    fun bindData(weatherResponseModel: WeatherResponseModel) {
        binding.apply {
            lifecycleOwner = this@MainActivity
            executePendingBindings()
            humidity.text=weatherResponseModel.main.humidity.toString()+"%"
            if (weatherResponseModel.weather.isNotEmpty()){
                weatherDescription.text= weatherResponseModel.weather[0].description
                weatherIcon.visibility= View.VISIBLE
                weatherIcon.loadImage(
                    "http://openweathermap.org/img/wn/${weatherResponseModel.weather[0].icon}@2x.png",
                    getProcessDrawable(weatherIcon.context)
                )
            }
            else
            {
                    weatherDescription.text= ""
                    weatherIcon.visibility= View.INVISIBLE
                }


            temp.text="${weatherResponseModel.main.temp.roundToInt()}\u00B0  "//for Fahrenheit  u2109 for celiouc u2103
            wind.text="${weatherResponseModel.wind.speed} kmph"
            windDirection.text=when(weatherResponseModel.wind.deg){
                in 0..45 -> "E"
                in 45..135 -> "N"
                in 135..225 -> "W"
                in 225..315 -> "S"
                in 315..360 -> "E"
                else-> ""
            }
            //presure mb
            pressure.text=weatherResponseModel.main.pressure.toString() +" mb"
            feelsLike.text=weatherResponseModel.main.feelsLike.roundToInt().toString() +" °"
            minMax.text="${weatherResponseModel.main.tempMin.roundToInt()}° / ${weatherResponseModel.main.tempMax.roundToInt()}°"

            searchView.setQuery(weatherResponseModel.name, false)


        }
    }

    fun observeLiveData(){
        viewModel.networkState.observe(this@MainActivity, Observer { it ->
            if (it != null) {
                when (it.status) {
                    NetworkState.Status.RUNNING -> {
                        ProgressLoading.show(this)
                        binding.noData.visibility=View.GONE
                    } // LOADING
                    NetworkState.Status.SUCCESS -> {
                        ProgressLoading.dismiss()
                        val data = it.data as? WeatherResponseModel
                        bindData(data!!)
                        binding.noData.visibility=View.GONE
                    }// LOADED
                    NetworkState.Status.FAILED -> {
                        ProgressLoading.dismiss()
                        bindData(WeatherResponseModel())
                        CustomToast.showErrorMessage(this, it.msg as String)
                        binding.noData.visibility=View.VISIBLE

                    } // FAILED
                }
                viewModel._networkState.value = null
            }
        })
    }

    fun getLocation() {

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null && location.time > Calendar.getInstance()
                .timeInMillis - 5 * 60 * 1000
        ) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
            viewModel.getDataFromServer(null, location.latitude.toString(), location.longitude.toString())
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