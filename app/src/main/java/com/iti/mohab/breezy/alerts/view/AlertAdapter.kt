package com.iti.mohab.breezy.alerts.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.alerts.viewmodel.AlertsViewModel
import com.iti.mohab.breezy.databinding.AlertCardBinding
import com.iti.mohab.breezy.model.WeatherAlert
import com.iti.mohab.breezy.util.convertLongToDayDate
import com.iti.mohab.breezy.util.convertLongToTime
import com.iti.mohab.breezy.util.getCurrentLocale
import com.iti.mohab.breezy.util.getSharedPreferences

class AlertAdapter(private val context: Context, private val viewModel: AlertsViewModel) :
    RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    var alertsList: List<WeatherAlert> = emptyList()

    class ViewHolder(val binding: AlertCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AlertAdapter.ViewHolder(
            AlertCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language: String = getSharedPreferences(context).getString(
            context.getString(R.string.languageSetting),
            getCurrentLocale(context)?.language
        )!!
        val alert = alertsList[position]
        Log.i("yoka", "onBindViewHolder: ${alert.id}")
        holder.binding.btnDelete.setOnClickListener {
            viewModel.deleteFavoriteWeather(alert.id!!)
        }
        holder.binding.textFrom.text =
            convertLongToDayDate(alert.startDate, language).plus(" ").plus(
                convertLongToTime(alert.startTime, language)
            )

        holder.binding.textTo.text = convertLongToDayDate(alert.endDate, language).plus(" ")
            .plus(convertLongToTime(alert.endTime, language))
    }

    override fun getItemCount(): Int {
        return alertsList.size
    }
}