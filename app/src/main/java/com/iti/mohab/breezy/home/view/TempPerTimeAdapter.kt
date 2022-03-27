package com.iti.mohab.breezy.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.databinding.TempPerTimeCardBinding
import com.iti.mohab.breezy.model.Hourly
import com.iti.mohab.breezy.util.convertNumbersToArabic
import com.iti.mohab.breezy.util.convertLongToTime
import com.iti.mohab.breezy.util.getIcon
import com.iti.mohab.breezy.util.getSharedPreferences

class TempPerTimeAdapter(private val context: Context) :
    RecyclerView.Adapter<TempPerTimeAdapter.ViewHolder>() {

    var hourly: List<Hourly> = emptyList()
    var temperatureUnit: String = ""
    private lateinit var language: String


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
        language = getSharedPreferences(context).getString(
            context.getString(
                R.string.languageSetting
            ), "en"
        )!!
        val hour = hourly[position + 1]
        holder.binding.imageCardTempIcon.setImageResource(getIcon(hour.weather[0].icon))
        if (language == "ar") {
            holder.binding.textCardTemp.text =
                convertNumbersToArabic(hour.temp.toInt()).plus(temperatureUnit)
        } else {
            holder.binding.textCardTemp.text = "${hour.temp.toInt()}".plus(temperatureUnit)
        }
        holder.binding.textCardTime.text = convertLongToTime(hour.dt, language).lowercase()
    }

    override fun getItemCount(): Int {
        var size = 0
        if (hourly.isNotEmpty()) {
//            for (i in 0..hourly.size) {
//                val time = convertLongToTime(hourly[i].dt, language).lowercase()
//                if (time == "11:00 pm" || time == "١١:٠٠ م") {
//                    size = i
//                    break
//                }
//            }
            size = 24
        }
        return size
    }

}