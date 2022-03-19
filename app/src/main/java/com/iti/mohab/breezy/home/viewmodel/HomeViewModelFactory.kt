package com.iti.mohab.breezy.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mohab.breezy.datasource.IWeatherRepository
import com.iti.mohab.breezy.datasource.MyLocationProvider

class HomeViewModelFactory(
    private val repository: IWeatherRepository,
    private val myLocationProvider: MyLocationProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, myLocationProvider) as T
    }
}