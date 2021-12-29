package com.example.bscthesis.describe_your_dog

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bscthesis.CurrentUserConstants
import com.example.bscthesis.R
import com.example.bscthesis.databinding.DescribeYourDogFragmentBinding
import com.example.bscthesis.databinding.LoginFragmentBinding
import com.example.bscthesis.registration.RegistrationFragmentDirections
import com.example.bscthesis.util.FirestoreUtil
import com.example.bscthesis.util.StorageUtil
import java.io.ByteArrayOutputStream

class DescribeYourDogFragment : Fragment() {
    private lateinit var binding: DescribeYourDogFragmentBinding
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
            R.layout.describe_your_dog_fragment,
            container,
            false
        )

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }

        binding.progressBar.hide()

        binding.describeYourDogAddPhoto.setOnClickListener{
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(Intent.createChooser(intent, "Select image"), RC_SELECT_IMAGE)
        }

        binding.describeYourDogRegisterButton.setOnClickListener {
            //get information from text fields
            val bread: String = binding.describeYourDogBreadField.editText?.text.toString()
            val sex: String = binding.describeYourDogSexField.editText?.text.toString()
            val age: String = binding.describeYourDogAgeField.editText?.text.toString()
            val size: String = binding.describeYourDogSizeField.editText?.text.toString()
            val neutered: String = binding.describeYourDogNeuteredField.editText?.text.toString()
            val notLike: String = binding.describeYourDogNotLikeField.editText?.text.toString()
            val beHereFor: String = binding.describeYourDogBeHereForField.editText?.text.toString()

            if (bread.isEmpty() || sex.isEmpty() || age.isEmpty() || size.isEmpty() || neutered.isEmpty() ||
                notLike.isEmpty() || beHereFor.isEmpty()
            ) {
                Toast.makeText(activity, "Fill all data", Toast.LENGTH_SHORT).show()
            } else {
                binding.progressBar.show()

                //initialize CurrentUserConstants
                CurrentUserConstants.USER_AGE = age
                CurrentUserConstants.USER_BREAD = bread
                CurrentUserConstants.USER_SEX = sex
                CurrentUserConstants.USER_SIZE = size
                CurrentUserConstants.USER_NEUTERED = neutered
                CurrentUserConstants.USER_NOT_LIKE = notLike
                CurrentUserConstants.USER_BE_HERE_FOR = beHereFor

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
                        CurrentUserConstants.PROFILE_PICTURE_PATH = imagePath
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
                    CurrentUserConstants.PROFILE_PICTURE_PATH = ""
                }
                findNavController().navigate(DescribeYourDogFragmentDirections.actionDescribeYourDogToMain())
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
                .into(binding.describeYourDogImageArea)

            pictureChanged = true
        }
    }
}
