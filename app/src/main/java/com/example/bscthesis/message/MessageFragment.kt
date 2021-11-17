package com.example.bscthesis.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MessageFragmentBinding

class MessageFragment : Fragment() {
    private lateinit var binding: MessageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.message_fragment,
            container,
            false
        )

        return binding.root
    }
}