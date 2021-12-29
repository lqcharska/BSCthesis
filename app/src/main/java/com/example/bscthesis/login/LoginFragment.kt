package com.example.bscthesis.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.CurrentUserConstants
import com.example.bscthesis.R
import com.example.bscthesis.databinding.LoginFragmentBinding
import com.example.bscthesis.registration.RegistrationFragmentDirections
import com.example.bscthesis.util.FirestoreUtil
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
                    //initialize CurrentUserConstants
                    if (it.isSuccessful){
                        FirestoreUtil.getCurrentUser { user ->
                            CurrentUserConstants.USER_NOT_LIKE = user.notLike
                            CurrentUserConstants.USER_NAME = user.name
                            CurrentUserConstants.PROFILE_PICTURE_PATH =
                                user.profilePicturePath.toString()
                            CurrentUserConstants.USER_AGE = user.age
                            CurrentUserConstants.USER_BREAD = user.bread
                            CurrentUserConstants.USER_SEX = user.sex
                            CurrentUserConstants.USER_NEUTERED = user.neutered
                            CurrentUserConstants.USER_BE_HERE_FOR = user.beHereFor
                            CurrentUserConstants.USER_SIZE = user.size
                        }
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