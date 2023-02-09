package com.wendychu.chatapplication.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.wendychu.chatapplication.R
import com.wendychu.chatapplication.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        setListener()
    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            login(email, password)
        }

        binding.btnRegister.setOnClickListener {
            clearText()
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login(email: String, password: String) {
        this.activity?.let {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it){ task ->
                    if (task.isSuccessful){
                        clearText()
                        findNavController().navigate(R.id.action_loginFragment_to_contactFragment)
                    } else {
                        Toast.makeText(it, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun clearText(){
        binding.etEmail.text.clear()
        binding.etPassword.text.clear()
    }
}