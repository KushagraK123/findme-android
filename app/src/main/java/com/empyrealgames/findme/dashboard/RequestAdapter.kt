package com.empyrealgames.findme.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.data.Request
import kotlinx.android.synthetic.main.frag_profile.view.tv_mobile
import kotlinx.android.synthetic.main.frag_profile.view.tv_username
import kotlinx.android.synthetic.main.requests_list_item.view.*

class RequestAdapter(private val myDataset: List<Request>,  val decline: (phone:String) -> Unit, val accept: (phone:String) -> Unit) :
    RecyclerView.Adapter<RequestAdapter.MyViewHolder>() {
    class MyViewHolder(requestListItem: View) : RecyclerView.ViewHolder(requestListItem){
        val tvMobile = requestListItem.tv_mobile
        val tvUsername = requestListItem.tv_username
        val bAccept = requestListItem.b_accept
        val bDecline = requestListItem.b_decline

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val requestView = LayoutInflater.from(parent.context)
            .inflate(R.layout.requests_list_item, parent, false)
        return MyViewHolder(requestView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.tvMobile.text = myDataset[position].phone
        holder.tvUsername.text = myDataset[position].phone
        holder.bAccept.setOnClickListener { println("Declining " + myDataset[position].phone) }
        holder.bDecline.setOnClickListener { decline(myDataset[position].phone) }
        holder.bAccept.setOnClickListener { accept(myDataset[position].phone) }
    }
    override fun getItemCount() = myDataset.size
}
