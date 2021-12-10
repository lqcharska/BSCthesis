package com.example.bscthesis.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.LoginFragmentBinding
import com.example.bscthesis.registration.RegistrationFragmentDirections
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        //Firebase authorization instance
        var mAuth = FirebaseAuth.getInstance()

        binding.loginLoginButton.setOnClickListener {
            //get information from text fields
            val emailAddress: String = binding.loginEmailField.editText?.text.toString()
            val password: String = binding.loginPasswordField.editText?.text.toString()

            //check if every fields if fulfill and contain no whitespace, if yes make a toast
            if(emailAddress.isBlank() || password.isBlank()){
                Toast.makeText(activity, "Fill all data", Toast.LENGTH_SHORT).show()
            } else {
                mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener{
                    if (!it.isSuccessful){
                        Toast.makeText(activity, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                    if (it.isSuccessful){
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginToMain()
                        )
                    }
                }
            }
            binding.loginEmailTextInput.setText("")
            binding.loginPasswordTextInput.setText("")
        }

        return binding.root
    }
}