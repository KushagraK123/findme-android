package com.empyrealgames.findme.connections

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.dashboard.data.Connection
import kotlinx.android.synthetic.main.connections_list_item.view.*
import kotlinx.android.synthetic.main.frag_profile.view.tv_mobile
import kotlinx.android.synthetic.main.frag_profile.view.tv_username
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.empyrealgames.findme.R


class ConnectionAdapter(private val myDataset: List<Connection>,  val delete: (phone:String) -> Unit,val context: Context, val onCardClick:(connection:Connection)->Unit) :
    RecyclerView.Adapter<ConnectionAdapter.MyViewHolder>() {

    class MyViewHolder(connectionListItem: View) : RecyclerView.ViewHolder(connectionListItem){

        val tvMobile = connectionListItem.tv_mobile
        val tvUsername = connectionListItem.tv_username
        val bMenu = connectionListItem.b_menu
        val card = connectionListItem.card

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val connectionView = LayoutInflater.from(parent.context)
            .inflate(R.layout.connections_list_item, parent, false)
        return MyViewHolder(
            connectionView
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.tvMobile.text = myDataset[position].phone
        holder.tvUsername.text = myDataset[position].phone
        println("setting values of item "  +  position)
        holder.bMenu.setOnClickListener { val builder = AlertDialog.Builder(context)
            builder.setTitle("")
            builder.setItems(arrayOf("Delete"),
                DialogInterface.OnClickListener { dialog, item ->
                  delete(myDataset[position].phone)
                })
            val alert = builder.create()
            alert.show() }
        holder.card.setOnClickListener { onCardClick(myDataset[position]) }

    }
    override fun getItemCount() = myDataset.size
}
