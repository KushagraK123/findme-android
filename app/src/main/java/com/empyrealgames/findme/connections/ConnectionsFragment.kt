package com.empyrealgames.findme.connections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.empyrealgames.findme.R
import com.empyrealgames.findme.dashboard.data.Connection
import com.empyrealgames.findme.dashboard.data.ConnectionViewModel
import kotlinx.android.synthetic.main.fragment_connections.*

class ConnectionsFragment : Fragment() {




    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var dataset: List<Connection>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*dataset = listOf()
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

        )*/

        rv_connections.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }


    fun deleteConnection(phone:String){
        connectionViewModel.deleteConnection(phone)
    }

}