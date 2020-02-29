package com.empyrealgames.findme.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.R
import kotlinx.android.synthetic.main.frag_profile.view.tv_mobile
import kotlinx.android.synthetic.main.frag_profile.view.tv_username
import kotlinx.android.synthetic.main.requests_list_item.view.*

class ContactsAdapter(private val myDataset: List<LocalContact>,  val sendRequest: (phone:String, name:String) -> Unit) :
    RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {
    class MyViewHolder(contactListItemView: View) : RecyclerView.ViewHolder(contactListItemView){
        val tvMobile = contactListItemView.tv_mobile
        val tvUsername = contactListItemView.tv_username
        val bAccept = contactListItemView.b_accept
        val bDecline = contactListItemView.b_decline

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val requestView = LayoutInflater.from(parent.context)
            .inflate(R.layout.requests_list_item, parent, false)
        return MyViewHolder(requestView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.tvMobile.text = myDataset[position].phone
        holder.tvUsername.text = myDataset[position].name
        holder.bAccept.text = "Add"
        holder.bDecline.isEnabled = false
        holder.bDecline.visibility = View.GONE
        holder.bAccept.setOnClickListener { sendRequest.invoke(myDataset[position].phone, myDataset[position].name) }
      //  holder.bDecline.setOnClickListener { decline(myDataset[position].phone) }
       // holder.bAccept.setOnClickListener { accept(myDataset[position].phone) }
    }
    override fun getItemCount() = myDataset.size
}
