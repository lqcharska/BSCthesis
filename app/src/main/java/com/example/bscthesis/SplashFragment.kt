package com.example.bscthesis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.databinding.SplashFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private lateinit var binding: SplashFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.splash_fragment,
            container,
            false
        )

        if(FirebaseAuth.getInstance().currentUser == null){
            findNavController().navigate(
                SplashFragmentDirections.actionSplashToGetStarted())
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        } else {
            findNavController().navigate(
                SplashFragmentDirections.actionSplashToMain())
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        return binding.root
    }
}



