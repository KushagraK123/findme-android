package com.empyrealgames.findme.dashboard

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.ConnectionsFragment
import com.empyrealgames.findme.dashboard.FragProfile
import com.empyrealgames.findme.dashboard.PagerAdapter
import com.empyrealgames.findme.dashboard.RequestsFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_profile_dashboard.*


class ProfileDashboard : Fragment(){


    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tab.tabGravity = TabLayout.GRAVITY_FILL
        pagerAdapter = PagerAdapter(activity!!.supportFragmentManager, tab.tabCount)
        p_profile_dashboard.adapter = pagerAdapter
        tab.setupWithViewPager(p_profile_dashboard)

    }

}
