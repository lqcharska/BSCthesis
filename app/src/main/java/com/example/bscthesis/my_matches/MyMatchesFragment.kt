package com.example.bscthesis.my_matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MyMatchesFragmentBinding
import com.example.bscthesis.match_profile.MatchProfileFragmentDirections

class MyMatchesFragment: Fragment() {

    private lateinit var binding: MyMatchesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.my_matches_fragment,
            container,
            false
        )

        binding.myMatchesReturnButton.setOnClickListener {
            findNavController().navigate(
                MatchProfileFragmentDirections.actionMatchProfileToMatchMe()
            )
        }

        return binding.root
    }
}