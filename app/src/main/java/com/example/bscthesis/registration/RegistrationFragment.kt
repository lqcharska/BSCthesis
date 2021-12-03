package com.example.bscthesis.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.bscthesis.R
import com.example.bscthesis.databinding.RegistrationFragmentBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

        var mAuth = FirebaseAuth.getInstance()


        binding.registrationNextButton.setOnClickListener {

            var emailAddress : String = binding.registrationEmailField.editText?.text.toString()
            var password : String = binding.registrationPasswordField.editText?.text.toString()
            var passwordConfirm : String = binding.registrationPasswordConfirmField.editText?.text.toString()
            var dogName : String = binding.registrationNameField.editText?.text.toString()
            if(emailAddress.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || dogName.isEmpty()){
                Toast.makeText(activity, "Fill all data", Toast.LENGTH_SHORT).show()
            } else {
                if (password != passwordConfirm){
                    Toast.makeText(activity, "Passwords are not the same", Toast.LENGTH_SHORT).show()

                }
                else{
                    mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener {
                        if (!it.isSuccessful){
                            Toast.makeText(activity, it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                        if(it.isSuccessful){
                            var userId : String = mAuth.currentUser?.uid.toString()
                            var currentUserDataBase : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                            var newUserInfo : HashMap<String, String> =  HashMap()
                            newUserInfo["dog_name"] = dogName

                            currentUserDataBase.setValue(newUserInfo).addOnFailureListener{
                                Toast.makeText(activity, "Failure, ${it.message}", Toast.LENGTH_LONG).show()
                            }

                        }
                    }
                }
                binding.registrationEmailTextInput.setText("")
                binding.registrationPasswordTextInput.setText("")
                binding.registrationPasswordConfirmTextInput.setText("")
                binding.registrationNameTextInput.setText("")
            }


//            findNavController().navigate(
//               RegistrationFragmentDirections.actionRegistrationToDescribeYourDog()
//            )
        }

        return binding.root
    }
}