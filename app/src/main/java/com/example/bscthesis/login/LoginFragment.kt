package com.example.bscthesis.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.LoginFragmentBinding
import com.example.bscthesis.registration.RegistrationFragmentDirections


class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        binding.loginLoginButton.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginToMain()
            )
        }

        return binding.root
    }
}