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
import com.empyrealgames.findme.dashboard.data.Connection
import com.empyrealgames.findme.dashboard.data.ConnectionViewModel
import com.empyrealgames.findme.databinding.FragLocationsPermissionsBinding
import com.empyrealgames.findme.firebase.deleteLocationPermission
import com.empyrealgames.findme.firebase.grantLocationPermission
import com.empyrealgames.findme.utils.showLoadingDialog
import kotlinx.android.synthetic.main.frag_profile.*


class LocationsPermissionFragment : Fragment() {

    lateinit var binding: FragLocationsPermissionsBinding
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var dataset: List<Connection>
    lateinit var loadingDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ):
            View? {
        return inflater.inflate(R.layout.frag_locations_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragLocationsPermissionsBinding.bind(view)
        loadingDialog =
            showLoadingDialog(context!!)
        dataset = listOf()
        viewAdapter =
            LocationPermissionAdapter(
                dataset,
                ::toggleLocationPermission
            )
        update()
    }

    fun update() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            viewManager = LinearLayoutManager(context)
            connectionViewModel = ViewModelProvider(activity!!).get(ConnectionViewModel::class.java)
            connectionViewModel.allConnections.observe(viewLifecycleOwner,
                Observer<List<Connection>> { t ->
                    viewAdapter =
                        LocationPermissionAdapter(
                            t,
                            ::toggleLocationPermission
                        )
                    rvLocationPermissions.adapter = viewAdapter
                    for (conn in t)
                        println("found connection with  " + conn.phone)
                }

            )


            rvLocationPermissions.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }

        }
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
        progressBar.visibility = View.GONE
    }

    fun initListeners() {
    }

    fun toggleLocationPermission(connection: Connection) {
        if (connection.locationPermissionGiven) {
            deleteLocationPermission(
                connection.phone,
                ::onDeletPermissionSuccess,
                ::onFailed
            )
        } else {
            grantLocationPermission(
                connection.phone,
                ::onLocationGrantSuccess,
                ::onFailed
            )

        }
        loadingDialog.show()
    }

    fun onDeletPermissionSuccess() {
        println("Deleted grant ")
        update()
    }

    fun onLocationGrantSuccess() {
        update()
        println("Location granted ")
    }

    fun onFailed() {
        update()
        Toast.makeText(context, "Something went wrong, Please try again later!", Toast.LENGTH_SHORT)
            .show()
    }


}
