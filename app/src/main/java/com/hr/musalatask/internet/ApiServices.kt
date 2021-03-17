package com.hr.musalatask.internet


import com.hr.musalatask.di.NetworkingModule.TOKEN
import com.hr.musalatask.model.WeatherResponseModel
import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface ApiServices {

    @GET("weather")
    fun getWeather(@Query("q")cityName:String?,
                         @Query("lat")lat:String?,
                         @Query("lon")lon:String?,
                         @Query("appid")token:String=TOKEN,
    @Query("units")type:String="metric"): Deferred<WeatherResponseModel>


}