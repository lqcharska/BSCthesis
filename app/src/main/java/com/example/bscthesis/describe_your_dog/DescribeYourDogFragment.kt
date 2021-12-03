package com.example.bscthesis.describe_your_dog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.DescribeYourDogFragmentBinding
import com.example.bscthesis.databinding.LoginFragmentBinding
import com.example.bscthesis.registration.RegistrationFragmentDirections

class DescribeYourDogFragment : Fragment() {
    private lateinit var binding: DescribeYourDogFragmentBinding

    override fun onResume() {
        super.onResume()

        //bread
        val breads = resources.getStringArray(R.array.breads)
        val breadArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, breads)
        binding.breadAutoTextView.setAdapter(breadArrayAdapter)

        //sex
        val sex = resources.getStringArray(R.array.sex)
        val sexArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, sex)
        binding.sexAutoTextView.setAdapter(sexArrayAdapter)

        //age
        val age = resources.getStringArray(R.array.age)
        val ageArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, age)
        binding.ageAutoTextView.setAdapter(ageArrayAdapter)

        //size
        val size = resources.getStringArray(R.array.size)
        val sizeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, size)
        binding.sizeAutoTextView.setAdapter(sizeArrayAdapter)

        //neutered
        val neutered = resources.getStringArray(R.array.neutered)
        val neuteredArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, neutered)
        binding.neuteredAutoTextView.setAdapter(neuteredArrayAdapter)

        //not like
        val notLike = resources.getStringArray(R.array.not_like)
        val notLikeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, notLike)
        binding.notLikeAutoTextView.setAdapter(notLikeArrayAdapter)

        //be here for
        val beHereFor = resources.getStringArray(R.array.be_here_for)
        val beHereForArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, beHereFor)
        binding.beHereForAutoTextView.setAdapter(beHereForArrayAdapter)

    }

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

        binding.describeYourDogRegisterButton.setOnClickListener {
            findNavController().navigate(
                DescribeYourDogFragmentDirections.actionDescribeYourDogToMain()
            )
        }

        return binding.root
    }
}
