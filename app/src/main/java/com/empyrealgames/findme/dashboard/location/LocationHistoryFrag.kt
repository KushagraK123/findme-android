package com.empyrealgames.findme.dashboard.location


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.data.ConnectionViewModel
import com.empyrealgames.findme.dashboard.data.Location
import com.empyrealgames.findme.databinding.FragLocationHistoryBinding
import com.empyrealgames.findme.firebase.sendLocationPermissionRequest
import kotlinx.android.synthetic.main.frag_location_history.*


class LocationHistoryFrag : Fragment() {
    private lateinit var binding:FragLocationHistoryBinding
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var dataset: List<Location>
    private  val args: LocationHistoryFragArgs  by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_location_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragLocationHistoryBinding.bind(view)
        binding.apply {
            if(args.Connection.locationPermissionAccess){
                loadLocations()
                vNoLocationAccess.cNoLocationPermission.visibility = View.GONE
                rvLocationHistory.visibility = View.VISIBLE
            }else{
                vNoLocationAccess.cNoLocationPermission.visibility = View.VISIBLE
                rvLocationHistory.visibility = View.GONE
                vNoLocationAccess.tvReqLocPer.text = "You have no access to ${args.Connection.phone}'s Location, would you like to send him her location request?"
                vNoLocationAccess.bReqLocPer.setOnClickListener { sendLocationPermissionRequest(
                    context!!, args.Connection.phone, ::onRequestSendSuccess, ::onRequestSendFailed ) }
            }
        }
    }


    fun onRequestSendSuccess(){

    }

    fun onRequestSendFailed(){

    }

    fun loadLocations(){

        dataset = listOf()
        viewManager = LinearLayoutManager(context)
        viewAdapter = LocationHistoryAdapter(
            dataset,
            ::seeOnMap
        )
        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)
        connectionViewModel.fetchLocations(args.Connection.phone)
        connectionViewModel.allLocations.observe( viewLifecycleOwner,
            Observer<List<Location>> { t ->
                viewAdapter = LocationHistoryAdapter(
                    t,
                    ::seeOnMap
                )
                rv_location_history.adapter = viewAdapter
            }
        )

        rv_location_history.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    fun seeOnMap(location: Location){
        println("clicked see on map ")
    }

}
