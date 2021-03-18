package com.hr.musalatask.views.weatherMainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hr.musalatask.internet.ApiServices
import com.hr.musalatask.internet.NetworkState
import com.hr.musalatask.model.WeatherResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apis: ApiServices) {
    private val _dataResponse = MutableLiveData(WeatherResponseModel())
    val dataResponse :LiveData<WeatherResponseModel> = _dataResponse
    suspend fun getData(
        cityName: String?,
        lat: String?,
        lon: String?
    ) {
        withContext(Dispatchers.IO){
            val response = apis.getWeather(cityName,lat,lon).await()
            _dataResponse.postValue(response)
        }
    }}