package com.example.bscthesis.match_me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MatchMeFragmentBinding
import com.example.bscthesis.login.LoginFragmentDirections


class MatchMeFragment : Fragment() {

    private lateinit var binding: MatchMeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.match_me_fragment,
            container,
            false
        )

        binding.matchMeReturnButton.setOnClickListener {
            findNavController().navigate(
                MatchMeFragmentDirections.actionMatchMeToMain()
            )
        }


        return binding.root
    }
}