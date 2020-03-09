package com.empyrealgames.findme.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.empyrealgames.findme.R
import com.empyrealgames.findme.databinding.FragmentManageLocationPermissionsBinding
import com.google.android.material.tabs.TabLayout


class ManageLocationPermissions : Fragment() {

    lateinit var bindings: FragmentManageLocationPermissionsBinding
    lateinit var tabAdapter: ManageLocationsPermissionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_location_permissions, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindings = FragmentManageLocationPermissionsBinding.bind(view)
        bindings.apply {
            tab.tabGravity = TabLayout.GRAVITY_FILL
            tabAdapter = ManageLocationsPermissionAdapter(activity!!.supportFragmentManager, tab.tabCount)
            vpLocationPermission.adapter = tabAdapter
            tab.setupWithViewPager(vpLocationPermission)
        }
    }
}