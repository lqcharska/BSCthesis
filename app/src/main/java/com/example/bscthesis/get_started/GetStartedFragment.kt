package com.example.bscthesis.get_started

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.GetStartedFragmentBinding


class GetStartedFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: GetStartedFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.get_started_fragment, container, false
        )
        binding.getStartedLoginButton.setOnClickListener {
            findNavController().navigate(
                GetStartedFragmentDirections.actionGetStartedToLogin()
            )
        }
        binding.getStartedRegisterButton.setOnClickListener {
            findNavController().navigate(
                GetStartedFragmentDirections.actionGetStartedToRegistration()
            )
        }

        return binding.root
    }

}
