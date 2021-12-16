package com.example.bscthesis.message_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bscthesis.AppConstants
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MessageProfileFragmentBinding
import com.example.bscthesis.util.StorageUtil

class MessageProfileFragment : Fragment() {
        private lateinit var binding: MessageProfileFragmentBinding

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            // Inflate view and obtain an instance of the binding class
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.message_profile_fragment,
                container,
                false
            )

            //set bar name
            binding.topAppBar.title = AppConstants.USER_NAME

            //set data
            binding.dogNameText.text = AppConstants.USER_NAME
            binding.ageTextInput.text = AppConstants.USER_AGE
            binding.beHereForTextInput.text = AppConstants.USER_BE_HERE_FOR
            binding.breadTextInput.text = AppConstants.USER_BREAD
            binding.neuteredTextInput.text = AppConstants.USER_NEUTERED
            binding.notLikeTextInput.text = AppConstants.USER_NOT_LIKE
            binding.sizeTextInput.text = AppConstants.USER_SIZE
            binding.sexTextInput.text = AppConstants.USER_SEX

            Glide.with(this)
                .load(StorageUtil.pathToReference(AppConstants.PROFILE_PICTURE_PATH))
                .placeholder(R.drawable.dog_profile_photo)
                .into(binding.messageProfileImageArea)

            

            return binding.root
        }
}