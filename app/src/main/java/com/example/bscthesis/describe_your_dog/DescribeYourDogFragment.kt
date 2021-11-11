package com.example.bscthesis.describe_your_dog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.bscthesis.R
import com.example.bscthesis.databinding.DescribeYourDogFragmentBinding
import com.example.bscthesis.databinding.LoginFragmentBinding

class DescribeYourDogFragment : Fragment() {
    private lateinit var binding: DescribeYourDogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.describe_your_dog_fragment,
            container,
            false
        )
        return binding.root
    }
}
