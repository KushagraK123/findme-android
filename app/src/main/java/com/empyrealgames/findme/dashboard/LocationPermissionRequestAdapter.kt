package com.empyrealgames.findme.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.data.LocationPermissionRequest
import kotlinx.android.synthetic.main.location_permission_request_list_item.view.*


class LocationPermissionRequestAdapter(private val myDataset: List<LocationPermissionRequest>,
                                       val onPositiveClicked :(LocationPermissionRequest)->Unit,
                                       val onNegativeClicked :((LocationPermissionRequest)->Unit)? = null
                                       ) :
    RecyclerView.Adapter<LocationPermissionRequestAdapter.MyViewHolder>() {

    class MyViewHolder(locationPermissionListItem: View) : RecyclerView.ViewHolder(locationPermissionListItem){
        val tvMobile = locationPermissionListItem.tv_mobile
        val tvUsername = locationPermissionListItem.tv_username
        val bAccept = locationPermissionListItem.b_accept
        val bDecline = locationPermissionListItem.b_accept
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val locationPermissionListItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_permission_request_list_item, parent, false)
        return MyViewHolder(
            locationPermissionListItemView
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.tvMobile.text = myDataset[position].phone
        holder.tvUsername.text = myDataset[position].phone
        println("Binding view hilder")
        holder.bAccept.setOnClickListener {
            println("Clicked positive")
            onPositiveClicked(myDataset[position])
        }
        println("setting values of item "  +  position)
        holder.bDecline.setOnClickListener { onNegativeClicked?.invoke(myDataset[position]) }
    }


    override fun getItemCount() = myDataset.size
}
