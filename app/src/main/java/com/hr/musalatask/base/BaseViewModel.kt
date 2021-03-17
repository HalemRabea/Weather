package com.hr.musalatask.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.musalatask.internet.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel : ViewModel() {
    internal val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    private val job = SupervisorJob()
    internal val viewModelScope = CoroutineScope(job + Dispatchers.Main)


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}