package com.iti.mohab.breezy.dialogs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mohab.breezy.datasource.MyLocationProvider

class DialogViewModelFactory(private val myLocationProvider: MyLocationProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DialogViewModel(myLocationProvider) as T
    }
}