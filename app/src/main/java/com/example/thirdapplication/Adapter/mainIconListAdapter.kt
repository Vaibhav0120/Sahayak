package com.example.thirdapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thirdapplication.Domain.ListDomain
import com.example.thirdapplication.R
import com.example.thirdapplication.databinding.ViewholderItemsBinding
import com.example.thirdapplication.ui.Events.EventFragment
import com.example.thirdapplication.ui.Machine_Learning.ML_Fragment
import com.example.thirdapplication.ui.dashboard.DashboardFragment

class MainIconListAdapter(private var items: MutableList<ListDomain>) :
    RecyclerView.Adapter<MainIconListAdapter.ViewHolder>() {

    class ViewHolder(var binding: ViewholderItemsBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Bind the data to the view
        holder.binding.titleTxtView.text = item.title
        holder.binding.descriptionTxtView.text = item.description
        holder.binding.DetailTxtView.text = item.detail

        // Get the drawable resource for the image
        val drawableResourceId = holder.itemView.resources.getIdentifier(
            item.pic, // Assuming 'pic' is the correct field for image
            "drawable",
            context.packageName
        )

        // Use Glide to load the image
        Glide.with(context)
            .load(drawableResourceId)
            .into(holder.binding.pic) // Ensure 'pic' is the correct ImageView reference

        // Handle item clicks
        holder.itemView.setOnClickListener {
            val fragment = when (position) {
                0 -> ML_Fragment()         // Navigate to ML Fragment
                2 -> DashboardFragment()   // Navigate to Dashboard Fragment
                3 -> EventFragment()       // Navigate to Event Fragment
                else -> return@setOnClickListener
            }

            // Replace the fragment in the container
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int = items.size
}
