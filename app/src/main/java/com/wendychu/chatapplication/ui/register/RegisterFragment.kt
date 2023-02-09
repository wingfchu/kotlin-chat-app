package com.wendychu.chatapplication.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wendychu.chatapplication.R
import com.wendychu.chatapplication.databinding.FragmentRegisterBinding
import com.wendychu.chatapplication.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        setListener()
    }

    private fun setListener() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            register(email, password)
        }
    }

    private fun register(email: String, password: String) {
        this.activity?.let {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(it){ task ->
                    if (task.isSuccessful){
                        addUserToDatabase(
                            binding.etName.text.toString(),
                            email,
                            auth.uid!!
                        )
                        clearText()
                        findNavController().navigate(R.id.action_registerFragment_to_contactFragment)
                    } else {
                        Toast.makeText(it, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String){
        dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("user").child(uid).setValue(User(name, email, uid))
    }

    private fun clearText(){
        binding.etName.text.clear()
        binding.etEmail.text.clear()
        binding.etPassword.text.clear()
    }
}