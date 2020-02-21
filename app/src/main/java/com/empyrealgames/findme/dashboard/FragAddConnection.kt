package com.empyrealgames.findme.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.empyrealgames.findme.R
import com.empyrealgames.findme.pref.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.frag_add_connection.*
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.content.ContentResolver
import androidx.core.widget.addTextChangedListener
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empyrealgames.findme.dashboard.connection.Connection
import com.empyrealgames.findme.dashboard.connection.ConnectionDao_Impl
import com.empyrealgames.findme.dashboard.connection.ConnectionViewModel
import com.empyrealgames.findme.dashboard.connection.Request
import com.empyrealgames.findme.showDialogUserNotFound
import kotlinx.android.synthetic.main.fragment_connections.*
import java.lang.StringBuilder


class FragAddConnection : Fragment() {

    var isRequestsInit = false
    var isConnectionsInit = false
    private lateinit var db: FirebaseFirestore
    private var contactsList = mutableListOf<LocalContact>()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var connectionViewModel: ConnectionViewModel
    private lateinit var requestList: Set<Request>
    private lateinit var connectionList: Set<Connection>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_add_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        connectionList = setOf()
        isConnectionsInit = false
        isRequestsInit = false
        requestList = setOf()
        navController = Navigation.findNavController(view)
        viewManager = LinearLayoutManager(context)
        connectionViewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)
        connectionViewModel.allConnections.observe( viewLifecycleOwner,
            Observer{ t ->
                connectionList = t.toSet()
                isConnectionsInit = true
                setUp()
            }

        )
        connectionViewModel.allRequests.observe( viewLifecycleOwner,
            Observer { t ->
                requestList = t.toSet()
                isRequestsInit = true
                setUp()
            }

        )
        et_number.editText!!.addTextChangedListener {
            textChanged(it.toString())
        }
    }

    fun textChanged(s:String){
        if(!s.isBlank()){
            println("searching for  "  +s)
            val searchList = mutableListOf<LocalContact>()
            contactsList.forEach{
                if(it.name.startsWith(s, true) || it.phone.startsWith(s)){
                    searchList.add(it)
                }
            }
            setUpWithList(searchList)
        }else{
            setUpWithList(contactsList)

        }
    }



    fun setUpWithList(list:List<LocalContact>){
            contactsAdapter = ContactsAdapter(list, ::sendRequest)
            rv_contacts.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = contactsAdapter
                println("searching for applied")
            }

    }

    fun setUp(){
        if(isRequestsInit && isConnectionsInit){
            val list = getListOfLocalContacts()
            contactsList = mutableListOf<LocalContact>()
            contactsList.addAll(list)
            list.forEach {
                if (connectionExists(Connection("+91" + it.phone))
                    || requestExists(Request("+91" + it.phone))
                ) {
                    contactsList.remove(it)
                    println("filtered list")

                }
            }
            contactsAdapter = ContactsAdapter(contactsList, ::sendRequest)
            rv_contacts.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = contactsAdapter
            }
        }
    }

    fun connectionExists(connection: Connection):Boolean{
        return connectionList.contains(connection)
    }
    fun requestExists(request: Request):Boolean{
        return requestList.contains(request)
    }

    fun fetchContactsList() {

        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    1
                )

            }
        } else {


        }
    }

    fun sendRequest(phone:String, name:String){
        val number = "+91" + phone
        db.collection("users").document(number).get().addOnCompleteListener {
            if (it.result!!.contains("uid")) {
                println("Yes user exists")
                db.collection("users").document(number)
                    .update(
                        "requests",
                        FieldValue.arrayUnion(PreferenceManager().getPhone(context!!))
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Request Sent",
                                Toast.LENGTH_LONG
                            ).show()
                            setUp()
                            println("request added successfully")
                        } else {
                            Toast.makeText(
                                context,
                                "Unable to send request at the moment!",
                                Toast.LENGTH_LONG
                            ).show()
                            println("request failed")

                        }
                    }

            } else {
                showDialogUserNotFound(context!!, phone, name, ::sendInvite).show()
                println("no user does not exists, show dialog")
            }
        }
    }
    fun sendInvite(){
        Toast.makeText(context!!, "Sending Invitation", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fetchContactsList()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }


    }


    private fun getListOfLocalContacts(): MutableList<LocalContact> {

        val returnList:MutableSet<LocalContact> = mutableSetOf()

        val resolver:ContentResolver = context!!.contentResolver
       /* var cursor =resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        /* Handle */
        while (cursor!!.moveToNext()){
            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

        }*/
        val phoneCursor =resolver.query(Phone.CONTENT_URI, null, null, null,  "upper("+ Phone.DISPLAY_NAME + ") ASC" )
        while (phoneCursor!!.moveToNext()){
            val phone = getFormattedPhoneNumber(phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER)))
            val name = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.DISPLAY_NAME))
            if(phone!=null) {
                returnList.add(LocalContact(phone, name))
            }
        }

        phoneCursor.close()



        return returnList.toMutableList()
    }

    fun getFormattedPhoneNumber(phone:String):String?{
        var ret = phone
        if(ret.startsWith("+91")){
            ret = ret.substring(3)
        }
        var ret2= StringBuilder()
        for(c in ret){
            if(c.isDigit()){
                ret2.append(c)
            }
        }
        if(ret2.length == 10){
            println("returning phome is " +ret2)
            return ret2.toString()

        }else{
            println("returning null as phome is " +ret2)
            return null
        }


    }

}