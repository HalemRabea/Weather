package com.hr.musalatask.views.weatherMainScreen

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.hr.musalatask.base.BaseViewModel
import com.hr.musalatask.internet.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val application: Application,
                                           val repo: WeatherRepository,
                                           private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    fun getDataFromServer(cityName:String?,lat:String?,lon:String?){
        viewModelScope.launch {
            // async {
            repo.getData(cityName,lat,lon){ networkState: NetworkState ->
                _networkState.value = networkState

            }
        }
    }
}
