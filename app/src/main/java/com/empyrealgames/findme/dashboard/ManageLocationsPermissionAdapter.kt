package com.empyrealgames.findme.dashboard


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ManageLocationsPermissionAdapter(fragmentManager: FragmentManager,numTabs:Int) :
    FragmentStatePagerAdapter(fragmentManager, numTabs){


    override fun getItem(position: Int): Fragment {
        var frag:Fragment? = null
        when(position){
            0->{frag = LocationPermissionRequestFragment()
            }
            1->{frag =  LocationsPermissionFragment()}

        }
        return frag!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title:String? = null
        when(position){
            0->{title =  "Location Permission Requests"}
            1->{title =  "Location Permission Access"}
        }
        return title!!

    }

    override fun getCount(): Int {
        return 2
    }
}