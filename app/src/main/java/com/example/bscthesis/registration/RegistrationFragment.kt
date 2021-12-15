package com.example.bscthesis.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bscthesis.R
import com.example.bscthesis.databinding.RegistrationFragmentBinding
import com.example.bscthesis.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth


class RegistrationFragment : Fragment() {

    private lateinit var binding: RegistrationFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.registration_fragment,
            container,
            false
        )

        binding.progressBar.hide()

        //Firebase authorization instance
        var mAuth = FirebaseAuth.getInstance()

        binding.registrationNextButton.setOnClickListener {
            //get information from text fields
            val emailAddress : String = binding.registrationEmailField.editText?.text.toString()
            val password : String = binding.registrationPasswordField.editText?.text.toString()
            val passwordConfirm : String = binding.registrationPasswordConfirmField.editText?.text.toString()
            val dogName : String = binding.registrationNameField.editText?.text.toString()

            //check if every fields is fulfill and contain no whitespace, if yes make a toast
            if(emailAddress.isBlank() || password.isBlank() || passwordConfirm.isBlank() || dogName.isBlank()){
                Toast.makeText(activity, "Fill all data", Toast.LENGTH_SHORT).show()
            } else {
                // check if passwords are the same, if not make a toast
                if (password != passwordConfirm){
                    Toast.makeText(activity, "Passwords are not the same", Toast.LENGTH_SHORT).show()
                    binding.progressBar.hide()
                } else {
                    binding.progressBar.show()
                    mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener {
                        if (!it.isSuccessful){
                            Toast.makeText(activity, it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                        if(it.isSuccessful){
                            FirestoreUtil.initCurrentUserIfItsFirstTime {
                                FirestoreUtil.nameYourDoggie(dogName)
                                findNavController().navigate(
                                    RegistrationFragmentDirections.actionRegistrationToDescribeYourDog()
                                )
                            }
                        }
                    }
                }
                binding.registrationEmailTextInput.setText("")
                binding.registrationPasswordTextInput.setText("")
                binding.registrationPasswordConfirmTextInput.setText("")
                binding.registrationNameTextInput.setText("")
            }
        }
        return binding.root
    }

}