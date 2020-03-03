package com.empyrealgames.findme.dashboard.location

import com.empyrealgames.findme.dashboard.data.Connection
import com.empyrealgames.findme.dashboard.data.Location
import kotlinx.android.synthetic.main.location_list_item.view.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.R

class LocationHistoryAdapter(private val myDataset: List<Location>, val seeOnMap: (location:Location) -> Unit) :
    RecyclerView.Adapter<LocationHistoryAdapter.MyViewHolder>() {
    class MyViewHolder(locationListItem: View) : RecyclerView.ViewHolder(locationListItem){
        val tvTime = locationListItem.tv_time
        val tvPlace = locationListItem.tv_place
        val bShowOnMap = locationListItem.b_show_on_map

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val requestView = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_list_item, parent, false)
        return MyViewHolder(
            requestView
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.tvTime.text = myDataset[position].time
        holder.tvPlace.text = myDataset[position].lat + " " + myDataset[position].long
        holder.bShowOnMap.setOnClickListener { seeOnMap(Location("12;30", "1.77777", "7.444454")) }
    }
    override fun getItemCount() = myDataset.size
}
