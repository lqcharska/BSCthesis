package com.example.bscthesis.match_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MatchProfileFragmentBinding
import com.example.bscthesis.match_me.MatchMeFragmentDirections

class MatchProfileFragment : Fragment() {

    private lateinit var binding: MatchProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.match_profile_fragment,
            container,
            false
        )

        binding.matchProfileReturnButton.setOnClickListener {
            findNavController().navigate(
                MatchProfileFragmentDirections.actionMatchProfileToMatchMe()
            )
        }

        binding.matchProfileReturnButton.setOnClickListener {
            findNavController().navigate(
                MatchProfileFragmentDirections.actionMatchProfileToMatchMe()
            )
        }

        binding.peeButton.setOnClickListener {
            findNavController().navigate(
                MatchProfileFragmentDirections.actionMatchProfileToMatchMe()
            )
        }

        return binding.root
    }
}