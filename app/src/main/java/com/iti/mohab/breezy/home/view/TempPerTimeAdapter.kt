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
        hour.weather[0].icon?.let { getIcon(it) }
            ?.let { holder.binding.imageCardTempIcon.setImageResource(it) }
        holder.binding.textCardTemp.text = "${hour.temp}"
        holder.binding.textCardTime.text = "${hour.dt?.let { convertLongToTime(it) }}"
    }

    override fun getItemCount(): Int {
        return hourly.size - 1
    }

}