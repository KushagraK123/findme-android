package com.empyrealgames.findme.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.data.ConnectionViewModel
import com.empyrealgames.findme.dashboard.data.LocationPermissionRequest
import com.empyrealgames.findme.databinding.FragLocationPermissionsRequestsBinding
import com.empyrealgames.findme.utils.showLoadingDialog

class LocationPermissionRequestFragment : Fragment() {

    private lateinit var loadingDialog:AlertDialog
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var binding:FragLocationPermissionsRequestsBinding
    private lateinit var dataset: List<LocationPermissionRequest>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_location_permissions_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragLocationPermissionsRequestsBinding.bind(view)
        loadingDialog =
            showLoadingDialog(context!!)
        dataset = mutableListOf()
        viewManager = LinearLayoutManager(context)
        viewAdapter = LocationPermissionRequestAdapter(
            dataset,
            onPositiveClicked =  ::acceptRequest,
            onNegativeClicked = ::declineRequest
        )

        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)
        connectionViewModel.allLocationPermissionRequests.observe( viewLifecycleOwner,
            Observer<List<LocationPermissionRequest>> { t ->
                viewAdapter = LocationPermissionRequestAdapter(
                    t,
                    onPositiveClicked = ::acceptRequest,
                    onNegativeClicked = ::declineRequest
                )
                binding.apply {
                    rvLocationPermissionRequests.adapter = viewAdapter
                }
            })
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvLocationPermissionRequests.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
            progressBar.visibility = View.GONE
        }
    }


    fun acceptRequest(locationPermissionRequest: LocationPermissionRequest){
        loadingDialog.show()
        connectionViewModel.acceptLocationPermissionRequest(locationPermissionRequest.phone, ::onSuccess, ::onFailed)
    }

    fun onSuccess(phone: String){
        connectionViewModel.deleteLocalLocationPermissionRequest(phone)
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
    }
    fun onFailed(){
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        Toast.makeText(context!!, "Something went wrong!", Toast.LENGTH_SHORT).show()
    }

    fun declineRequest(locationPermissionRequest: LocationPermissionRequest){
        loadingDialog.show()
        connectionViewModel.deleteLocationPermissionRequest(locationPermissionRequest.phone, ::onSuccess, ::onFailed)
    }

}

