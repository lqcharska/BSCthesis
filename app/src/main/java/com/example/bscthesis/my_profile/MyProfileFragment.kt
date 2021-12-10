package com.example.bscthesis.my_profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MyProfileFragmentBinding
import com.example.bscthesis.model.User
import com.example.bscthesis.util.FirestoreUtil
import com.example.bscthesis.util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import io.grpc.Context
import java.io.ByteArrayOutputStream

class MyProfileFragment: Fragment() {

    private lateinit var binding: MyProfileFragmentBinding

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes : ByteArray
    private var pictureChanged = false

    override fun onResume() {
        super.onResume()

        //bread
        val listOfBreads = resources.getStringArray(R.array.breads)
        val breadArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, listOfBreads)
        binding.breadAutoTextView.setAdapter(breadArrayAdapter)

        //sex
        val listOfSex = resources.getStringArray(R.array.sex)
        val sexArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, listOfSex)
        binding.sexAutoTextView.setAdapter(sexArrayAdapter)

        //age
        val listOfAge = resources.getStringArray(R.array.age)
        val ageArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, listOfAge)
        binding.ageAutoTextView.setAdapter(ageArrayAdapter)

        //size
        val listOfSize = resources.getStringArray(R.array.size)
        val sizeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, listOfSize)
        binding.sizeAutoTextView.setAdapter(sizeArrayAdapter)

        //neutered
        val listOfNeutered = resources.getStringArray(R.array.neutered)
        val neuteredArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, listOfNeutered)
        binding.neuteredAutoTextView.setAdapter(neuteredArrayAdapter)

        //not like
        val listOfNotLike = resources.getStringArray(R.array.not_like)
        val notLikeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, listOfNotLike)
        binding.notLikeAutoTextView.setAdapter(notLikeArrayAdapter)

        //be here for
        val listOfBeHereFor = resources.getStringArray(R.array.be_here_for)
        val beHereForArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, listOfBeHereFor)
        binding.beHereForAutoTextView.setAdapter(beHereForArrayAdapter)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.my_profile_fragment,
            container,
            false
        )

        binding.progressBar.hide()

        //Firebase authorization instance
        var mAuth = FirebaseAuth.getInstance()

        binding.myProfileAddPhoto.setOnClickListener{
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(Intent.createChooser(intent, "Select image"), RC_SELECT_IMAGE)
        }

        binding.myProfileLogoutButton.setOnClickListener {
            mAuth.signOut()
            findNavController().navigate(
                MyProfileFragmentDirections.actionMyProfileToLogin()
            )
        }


        binding.myProfileReturnButton.setOnClickListener{
            findNavController().navigate(
                MyProfileFragmentDirections.actionMyProfileToMain()
            )
        }

        binding.myProfileSaveButton.setOnClickListener {
            //get information from text fields
            val bread: String = binding.myProfileBreadField.editText?.text.toString()
            val sex: String = binding.myProfileSexField.editText?.text.toString()
            val age: String = binding.myProfileAgeField.editText?.text.toString()
            val size: String = binding.myProfileSizeField.editText?.text.toString()
            val neutered: String = binding.myProfileNeuteredField.editText?.text.toString()
            val notLike: String = binding.myProfileNotLikeField.editText?.text.toString()
            val beHereFor: String = binding.myProfileBeHereForField.editText?.text.toString()

            if (bread.isEmpty() || sex.isEmpty() || age.isEmpty() || size.isEmpty() || neutered.isEmpty() ||
                notLike.isEmpty() || beHereFor.isEmpty()
            ) {
                Toast.makeText(activity, "Fill all data", Toast.LENGTH_SHORT).show()
            } else {
                binding.progressBar.show()
                if (::selectedImageBytes.isInitialized) {
                    StorageUtil.uploadProfilePhoto(selectedImageBytes)
                    { imagePath ->
                        FirestoreUtil.describeDoggie(
                            bread,
                            sex,
                            age,
                            size,
                            neutered,
                            notLike,
                            beHereFor,
                            imagePath
                        )
                    }
                } else {
                    FirestoreUtil.describeDoggie(
                        bread,
                        sex,
                        age,
                        size,
                        neutered,
                        notLike,
                        beHereFor,
                        null
                    )
                }
                Toast.makeText(activity, "Saving", Toast.LENGTH_SHORT).show()
                binding.progressBar.hide()
            }
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            val selectedImagePath =data.data
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            Glide.with(this)
                .load(selectedImageBmp)
                .circleCrop()
                .into(binding.myProfileImageArea)

            pictureChanged = true
        }
    }

    override fun onStart() {
        super.onStart()

        FirestoreUtil.getCurrentUser { user ->
            if (this@MyProfileFragment.isVisible){
                binding.myProfileText.text = user.name
                binding.breadAutoTextView.setText(user.bread, false)
                binding.sexAutoTextView.setText(user.sex, false)
                binding.ageAutoTextView.setText(user.age, false)
                binding.sizeAutoTextView.setText(user.size, false)
                binding.neuteredAutoTextView.setText(user.neutered, false)
                binding.notLikeAutoTextView.setText(user.notLike, false)
                binding.beHereForAutoTextView.setText(user.beHereFor, false)
                if (!pictureChanged && user.profilePicturePath != null){
                    Glide.with(this)
                        .load(StorageUtil.pathToReference(user.profilePicturePath))
                        .placeholder(R.drawable.dog_profile_photo)
                        .circleCrop()
                        .into(binding.myProfileImageArea)

                }

            }
        }
    }
}