package com.empyrealgames.findme.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.empyrealgames.findme.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.Location.LocationFetchService
import com.empyrealgames.findme.dashboard.data.Connection
import com.empyrealgames.findme.dashboard.data.ConnectionViewModel
import com.empyrealgames.findme.dashboard.data.Location
import kotlinx.android.synthetic.main.frag_dash.*
import kotlinx.android.synthetic.main.frag_dash.rv_connections
import kotlinx.android.synthetic.main.toolbar.*

class FragDash : Fragment(), View.OnClickListener {

    val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var dataset: List<Connection>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_dash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        checkLocationPermissionAndUpdateData()
        dataset = listOf()
        println("Inside conn frag ")
        viewAdapter = ConnectionAdapter(dataset, ::deleteConnection, context!!)
        viewManager = LinearLayoutManager(context)
        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)
        connectionViewModel.allConnections.observe( viewLifecycleOwner,
            Observer<List<Connection>> { t ->
                if(context!=null)
                    viewAdapter = ConnectionAdapter(t, ::deleteConnection, context!!)
                rv_connections.adapter = viewAdapter
                for(conn in t)
                    println("found connection with  " +  conn.phone)
            }

        )

        rv_connections.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        connectionViewModel.fetchLocations("+917447554968")

        connectionViewModel.allLocations.observe( viewLifecycleOwner,
            Observer<List<Location>> { t ->
                for(loc in t)
                    println("found location    " + loc)
            }

        )


    }

    fun initListeners(){
        fab_add_conn.setOnClickListener(this)
        fab_profile_pic.setOnClickListener(this)
    }

    fun deleteConnection(phone:String){
        connectionViewModel.deleteConnection(phone)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab_add_conn ->{ findNavController().navigate(R.id.action_fragDash_to_fragAddConnection) }
            R.id.fab_profile_pic ->{ findNavController().navigate(R.id.action_fragDash_to_profiileDashboard) }
        }
    }

    fun checkLocationPermissionAndUpdateData() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQUEST_CODE
                )

            }
        }else{
            context!!.startService(Intent(activity, LocationFetchService::class.java))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            context!!.startService(Intent(activity, LocationFetchService::class.java))
        }

    }
}