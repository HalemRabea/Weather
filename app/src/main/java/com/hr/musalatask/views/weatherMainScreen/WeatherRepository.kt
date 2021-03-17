package com.hr.musalatask.views.weatherMainScreen

import com.hr.musalatask.internet.ApiServices
import com.hr.musalatask.internet.NetworkState
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apis: ApiServices) {
    suspend fun getData(
        cityName: String?,
        lat: String?,
        lon: String?,
        onNetworkState: (NetworkState) -> Unit
    ) {
        onNetworkState(NetworkState.LOADING)
        try {
            val response = apis.getWeather(cityName,lat,lon).await()
            onNetworkState(NetworkState.getLoaded(response))

        } catch (t: Throwable) {
            onNetworkState(NetworkState.getErrorMessage(t))
        }
    }}