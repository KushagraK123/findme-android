package com.empyrealgames.findme.dashboard

import android.app.DownloadManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.empyrealgames.findme.dashboard.connection.Connection
import com.empyrealgames.findme.dashboard.connection.ConnectionViewModel
import com.empyrealgames.findme.R
import androidx.lifecycle.Observer
import com.empyrealgames.findme.dashboard.connection.Request
import kotlinx.android.synthetic.main.frag_dash.*

class FragDash : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var connectionViewModel: ConnectionViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_dash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)


        b_add_connection.setOnClickListener(this)
        b_profile.setOnClickListener(this)
        tv_profile.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.b_add_connection -> navController.navigate(R.id.action_fragDash_to_fragAddConnection)
            R.id.b_profile -> navController.navigate(R.id.action_fragDash_to_profiileDashboard)
    //        R.id.tv_profile -> navController.navigate(R.id.action_fragDash_to_fragProfile)

        }
    }

}