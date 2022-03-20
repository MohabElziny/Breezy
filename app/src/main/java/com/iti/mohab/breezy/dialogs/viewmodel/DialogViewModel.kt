package com.iti.mohab.breezy.dialogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iti.mohab.breezy.datasource.MyLocationProvider

class DialogViewModel(private val myLocationProvider: MyLocationProvider) : ViewModel() {

    fun getFreshLocation() {
        myLocationProvider.getFreshLocation()
    }

    fun observeLocation():LiveData<ArrayList<Double>>{
        return myLocationProvider.locationList
    }

    fun observePermission():LiveData<String>{
        return myLocationProvider.denyPermission
    }

}