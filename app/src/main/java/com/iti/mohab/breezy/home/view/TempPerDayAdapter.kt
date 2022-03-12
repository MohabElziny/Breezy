package com.iti.mohab.breezy.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.mohab.breezy.databinding.TempPerDayCardBinding
import com.iti.mohab.breezy.model.Daily
import com.iti.mohab.breezy.util.getIcon
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TempPerDayAdapter(private val context: Context) :
    RecyclerView.Adapter<TempPerDayAdapter.ViewHolder>() {

    var daily: List<Daily> = emptyList()

    class ViewHolder(val binding: TempPerDayCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TempPerDayCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daily[position + 1]

        day.weather[0]?.icon?.let { getIcon(it) }
            ?.let { holder.binding.imageCardDayIcon.setImageResource(it) }
        holder.binding.textCardDay.text = day.dt?.let { convertLongToDay(it) }
        holder.binding.textCardDayTempDescription.text = day.weather[0].description
        holder.binding.textCardDayTemp.text = day.temp?.day.toString()
    }

    override fun getItemCount(): Int {
        return daily.size - 1
    }

    private fun convertLongToDay(time: Long): String {
        val date = Date(TimeUnit.SECONDS.toMillis(time))
        val format = SimpleDateFormat("EEE, d MMM yyyy")
        return format.format(date)
    }
}