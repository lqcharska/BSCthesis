package com.example.bscthesis.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.LoginFragmentBinding
import com.example.bscthesis.databinding.RegistrationFragmentBinding
import com.example.bscthesis.get_started.GetStartedFragmentDirections

class RegistrationFragment : Fragment() {
    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.registration_fragment,
            container,
            false
        )

        binding.registrationNextButton.setOnClickListener {
            findNavController().navigate(
               RegistrationFragmentDirections.registrationDestinationToDescribeYourDog()
            )
        }

        return binding.root
    }
}