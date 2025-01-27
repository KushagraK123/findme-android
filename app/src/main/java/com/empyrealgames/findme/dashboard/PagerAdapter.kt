package com.empyrealgames.findme.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.empyrealgames.findme.requests.RequestsFragment

class PagerAdapter(fragmentManager: FragmentManager,numTabs:Int) :
    FragmentStatePagerAdapter(fragmentManager, numTabs){


    override fun getItem(position: Int): Fragment {
        var frag:Fragment? = null
        when(position){
            0->{frag = RequestsFragment()
            }
            1->{frag =  FragProfile()}
            2 -> {
                frag = ManageLocationPermissions()
            }
        }
        return frag!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title:String? = null
        when(position){
            0->{title =  "Requests"}
            1->{title =  "Profile"}
            2 -> {
                title = "Location Permission"
            }
        }
        return title!!

    }

    override fun getCount(): Int {
        return 3
    }
}