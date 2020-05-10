package com.empyrealgames.findme.connections

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.empyrealgames.findme.utils.showLoadingDialog
import kotlinx.android.synthetic.main.frag_dash.*
import kotlinx.android.synthetic.main.frag_dash.rv_connections
import kotlinx.android.synthetic.main.toolbar.*

class FragDash : Fragment(), View.OnClickListener {

    val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var dataset: List<Connection>
    private lateinit var loadingDialog: AlertDialog

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
        loadingDialog =
            showLoadingDialog(context!!)
        println("Inside conn frag ")
        viewAdapter = ConnectionAdapter(
            dataset,
            ::deleteConnection,
            context!!,
            ::onCardClick
        )
        viewManager = LinearLayoutManager(context)
        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)
        connectionViewModel.allConnections.observe( viewLifecycleOwner,
            Observer<List<Connection>> { t ->
                if(context!=null)
                    viewAdapter =
                        ConnectionAdapter(
                            t,
                            ::deleteConnection,
                            context!!,
                            ::onCardClick
                        )
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

//        connectionViewModel.allLocations.observe( viewLifecycleOwner,
//            Observer<List<Location>> { t ->
//                for(loc in t)
//                    println("found location    " + loc)
//            }
//
//        )


    }

    fun onCardClick(connection: Connection){
        println("Clicked on card "  + connection.toString())
        val direction = FragDashDirections.actionFragDashToLocationHistoryFrag(connection)
        findNavController().navigate(direction)
    }

    fun initListeners(){
        fab_add_conn.setOnClickListener(this)
        fab_profile_pic.setOnClickListener(this)
    }

    fun deleteConnection(phone:String){
        loadingDialog.show()
        connectionViewModel.deleteConnection(phone, ::onSuccess, ::onFailed)
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
                    Manifest.permission.ACCESS_COARSE_LOCATION
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

    fun onSuccess(phone: String){
        connectionViewModel.deleteLocalConnection(phone)
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        Toast.makeText(context!!, "Deleted connections successfully!", Toast.LENGTH_LONG).show()
    }
    fun onFailed(){
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        Toast.makeText(context!!, "Cant delete connection", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            context!!.startService(Intent(activity, LocationFetchService::class.java))
        }

    }
}