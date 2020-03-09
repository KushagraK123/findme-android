package com.empyrealgames.findme.dashboard


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.dashboard.data.Connection
import kotlinx.android.synthetic.main.frag_profile.view.tv_mobile
import kotlinx.android.synthetic.main.frag_profile.view.tv_username
import com.empyrealgames.findme.R
import kotlinx.android.synthetic.main.location_permission_list_item.view.*


class LocationPermissionAdapter(private val myDataset: List<Connection>, val onRadioClicked :(Connection)->Unit) :
    RecyclerView.Adapter<LocationPermissionAdapter.MyViewHolder>() {

    class MyViewHolder(locationPermissionListItem: View) : RecyclerView.ViewHolder(locationPermissionListItem){
        val tvMobile = locationPermissionListItem.tv_mobile
        val tvUsername = locationPermissionListItem.tv_username
        val bToggleLocationPermission = locationPermissionListItem.b_toggle_location_permission
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val locationPermissionListItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_permission_list_item, parent, false)
        return MyViewHolder(
            locationPermissionListItemView
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.tvMobile.text = myDataset[position].phone
        holder.tvUsername.text = myDataset[position].phone
        holder.bToggleLocationPermission.isChecked = myDataset[position].locationPermissionGiven
        println("setting values of item "  +  position)
        holder.bToggleLocationPermission.setOnClickListener { onRadioClicked(myDataset[position]) }
    }


    override fun getItemCount() = myDataset.size
}
