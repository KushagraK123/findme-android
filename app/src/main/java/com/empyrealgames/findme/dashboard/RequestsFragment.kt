package com.empyrealgames.findme.dashboard

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.service.autofill.Dataset
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.connection.ConnectionViewModel
import com.empyrealgames.findme.dashboard.connection.Request
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.fragment_requests.*


class RequestsFragment : Fragment() {


    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var dataset: List<Request>

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
        viewManager = LinearLayoutManager(context)
        viewAdapter = RequestAdapter(dataset, ::declineRequest, ::acceptRequest)
        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)
        connectionViewModel.allRequests.observe( viewLifecycleOwner,
            Observer<List<Request>> { t ->
             viewAdapter = RequestAdapter(t, ::declineRequest, ::acceptRequest)
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
        connectionViewModel.acceptRequest(phone)
    }

    fun declineRequest(phone:String){
        println("click decline request, daahhhh")
        connectionViewModel.deleteRequest(phone)
    }


}
