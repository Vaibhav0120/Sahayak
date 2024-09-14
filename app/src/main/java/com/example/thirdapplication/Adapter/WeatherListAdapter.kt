package com.example.thirdapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thirdapplication.Domain.WeatherDomain
import com.example.thirdapplication.R
import com.example.thirdapplication.databinding.ViewholderWeatherBinding
import com.example.thirdapplication.ui.Machine_Learning.ML_Fragment

class WeatherListAdapter(private var items: MutableList<WeatherDomain>) :
    RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {

    class ViewHolder(var binding: ViewholderWeatherBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderWeatherBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherItem = items[position]

        // Bind the data to the view
        holder.binding.weatherTitleTextView.text = weatherItem.title
        holder.binding.weatherDescriptionTextView.text = weatherItem.description

        // Get the drawable resource for the weather icon
        val drawableResourceId = holder.itemView.resources.getIdentifier(
            weatherItem.icon, // Assuming 'icon' contains the drawable name
            "drawable",
            context.packageName
        )

        // Use Glide to load the weather icon
        Glide.with(context)
            .load(drawableResourceId)
            .into(holder.binding.weatherIconImageView)

        // Handle item clicks
        holder.itemView.setOnClickListener {
            if (position == 0) {  // Example click action for first item
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, ML_Fragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
