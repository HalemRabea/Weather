package com.hr.musalatask.views.weatherMainScreen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.hr.musalatask.base.BaseViewModel
import com.hr.musalatask.internet.NetworkState
import com.hr.musalatask.model.WeatherResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val application: Application,
                                           val repo: WeatherRepository,
                                           private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val weatherData=repo.dataResponse
    fun getDataFromServer(cityName:String?,lat:String?,lon:String?){
        viewModelScope.launch {
            _networkState.value=(NetworkState.LOADING)
            try {
                repo.getData(cityName,lat,lon)
                _networkState.value=(NetworkState.getLoaded(weatherData))

            } catch (t: Throwable) {
                _networkState.value=(NetworkState.getErrorMessage(t))
            }
        }
    }
}
