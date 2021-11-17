package com.example.bscthesis.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MainFragmentBinding
import com.example.bscthesis.registration.RegistrationFragmentDirections


class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )

        binding.mainMatchMeButton.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainToMatchMe()
            )
        }

        binding.mainMyMatchesButton.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainToMyMatches()
            )
        }

        return binding.root
    }
}