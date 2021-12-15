package com.example.bscthesis.recycleview.item

import android.content.Context
import android.util.Log
import android.view.View
import com.example.bscthesis.R
import com.example.bscthesis.databinding.DogsItemBinding
import com.example.bscthesis.glide.GlideApp
import com.example.bscthesis.model.User
import com.example.bscthesis.util.StorageUtil
import com.xwray.groupie.viewbinding.BindableItem

class DogItem (val dog: User, val userId: String, private val context: Context) : BindableItem<DogsItemBinding>() {


    override fun bind(binding: DogsItemBinding, position: Int) {
        binding.dogName.text = dog.name
        if (dog.profilePicturePath != null){
            GlideApp.with(context)
                .load(StorageUtil.pathToReference(dog.profilePicturePath))
                .placeholder(R.drawable.dog_profile_photo)
                .circleCrop()
                .into(binding.dogPhoto)
        }
    }

    override fun getLayout(): Int {
        return R.layout.dogs_item
    }

    override fun initializeViewBinding(view: View): DogsItemBinding {
        return DogsItemBinding.bind(view)
    }

}

