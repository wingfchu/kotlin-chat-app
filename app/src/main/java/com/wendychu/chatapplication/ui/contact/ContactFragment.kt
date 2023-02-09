package com.wendychu.chatapplication.ui.contact

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wendychu.chatapplication.R
import com.wendychu.chatapplication.databinding.FragmentContactListBinding
import com.wendychu.chatapplication.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ContactFragment : Fragment(R.layout.fragment_contact_list) {
    private val contactList = arrayListOf<User>()

    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactListAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactListBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        adapter = ContactListAdapter(contactList, ::onUserItemClick)
        binding.rvContact.adapter = adapter
        dbRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                contactList.clear()
                for (postSnapShot in snapshot.children){
                    val currentUser = postSnapShot.getValue(User::class.java)
                    if (auth.currentUser?.uid != currentUser?.uid)
                        contactList.add(currentUser!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_logout -> {
                auth.signOut()
                findNavController().navigate(R.id.action_contactFragment_to_loginFragment)
                true
            }
            else -> true
        }
    }

    private fun onUserItemClick(user: User) {
        val bundle = bundleOf("user" to user.name, "uid" to user.uid)
        findNavController().navigate(R.id.action_contactFragment_to_chatFragment, bundle)
    }
}