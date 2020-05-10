package com.empyrealgames.findme.requests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.data.ConnectionViewModel
import com.empyrealgames.findme.dashboard.data.Request
import com.empyrealgames.findme.utils.showLoadingDialog
import kotlinx.android.synthetic.main.fragment_requests.*


class RequestsFragment : Fragment() {


    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var dataset: List<Request>
    private lateinit var loadingDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataset = listOf()
        loadingDialog =
            showLoadingDialog(context!!)
        viewManager = LinearLayoutManager(context)
        viewAdapter = RequestAdapter(
            dataset,
            ::declineRequest,
            ::acceptRequest
        )
        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)
        connectionViewModel.allRequests.observe( viewLifecycleOwner,
            Observer<List<Request>> { t ->
             viewAdapter = RequestAdapter(
                 t,
                 ::declineRequest,
                 ::acceptRequest
             )
                rv_requests.adapter = viewAdapter
            }



        )

        rv_requests.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    fun acceptRequest(phone: String){
        loadingDialog.show()
        connectionViewModel.acceptRequest(phone, ::onSuccessAccept, ::onFailedAccept)
    }

    fun declineRequest(phone:String){
        loadingDialog.show()
        connectionViewModel.deleteRequest(phone, ::onSuccessDecline, ::onFailedDecline)
    }

    fun onSuccessAccept(phone: String){
        connectionViewModel.deleteLocalRequest(phone)
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        Toast.makeText(context!!, "accept connections request successfully!", Toast.LENGTH_LONG).show()
    }
    fun onFailedAccept(){
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        Toast.makeText(context!!, "Cant accept acsc connection", Toast.LENGTH_SHORT).show()
    }

    fun onSuccessDecline(phone: String){
        connectionViewModel.deleteLocalRequest(phone)
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        Toast.makeText(context!!, "Deleted connections sj successfully!", Toast.LENGTH_LONG).show()
    }
    fun onFailedDecline(){
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        Toast.makeText(context!!, "Cant delete hasgc connection", Toast.LENGTH_SHORT).show()
    }
}
