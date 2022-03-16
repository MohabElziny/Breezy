package com.iti.mohab.breezy.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.mohab.breezy.databinding.FavoriteCardBinding
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.util.getCityText

class FavoriteAdapter(private val context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    var favoriteList: List<OpenWeatherApi> = emptyList()

    class ViewHolder(val binding: FavoriteCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FavoriteCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favoriteList[position]
        holder.binding.textFavoriteCountry.text = getCityText(context, favorite.lat, favorite.lon)
        holder.binding.btnDelete.setOnClickListener {}
        holder.binding.favoriteCardView.setOnClickListener {}
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }
}