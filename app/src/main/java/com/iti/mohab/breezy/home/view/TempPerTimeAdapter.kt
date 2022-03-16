package com.iti.mohab.breezy.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.mohab.breezy.databinding.TempPerTimeCardBinding
import com.iti.mohab.breezy.model.Hourly
import com.iti.mohab.breezy.util.convertLongToTime
import com.iti.mohab.breezy.util.getIcon

class TempPerTimeAdapter(private val context: Context) :
    RecyclerView.Adapter<TempPerTimeAdapter.ViewHolder>() {

    var hourly: List<Hourly> = emptyList()
    var temperatureUnit: String = ""


    class ViewHolder(val binding: TempPerTimeCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TempPerTimeCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hour = hourly[position + 1]

        holder.binding.imageCardTempIcon.setImageResource(getIcon(hour.weather[0].icon))
        holder.binding.textCardTemp.text = "${hour.temp}".plus(temperatureUnit)
        holder.binding.textCardTime.text = convertLongToTime(hour.dt).lowercase()
    }

    override fun getItemCount(): Int {
        var size = 0
        if (hourly.isNotEmpty()) {
            for (i in 0..hourly.size) {
                if (convertLongToTime(hourly[i].dt).lowercase() == "11:00 pm") {
                    size = i
                    break
                }
            }
        }
        return size
    }

}