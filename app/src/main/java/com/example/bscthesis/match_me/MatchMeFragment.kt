package com.example.bscthesis.match_me

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bscthesis.AppConstants
import com.example.bscthesis.R
import com.example.bscthesis.databinding.DogsItemBinding
import com.example.bscthesis.databinding.MatchMeFragmentBinding
import com.example.bscthesis.login.LoginFragmentDirections
import com.example.bscthesis.recycleview.item.DogItem
import com.example.bscthesis.util.FirestoreUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.protobuf.ApiProto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem


class MatchMeFragment : Fragment() {

    private lateinit var binding: MatchMeFragmentBinding
    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var dogSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.match_me_fragment,
            container,
            false
        )

        userListenerRegistration = FirestoreUtil.addUsersListener(this.requireActivity(),this::updateRecycleView)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecycleView(items: List<BindableItem<DogsItemBinding>>){
        fun init(){
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MatchMeFragment.context)
                adapter = GroupieAdapter().apply {
                    dogSection = Section(items)
                    add(dogSection)
                    setOnItemClickListener(onClickItem)

                }
            }
            shouldInitRecyclerView = false

        }

        fun updateItems() = dogSection.update(items)

        if (shouldInitRecyclerView) init()
        else updateItems()
    }

    private val onClickItem = OnItemClickListener {item, view ->
        if (item is DogItem){
            AppConstants.USER_NAME = item.dog.name
            AppConstants.USER_AGE = item.dog.age
            AppConstants.USER_BE_HERE_FOR = item.dog.beHereFor
            AppConstants.USER_BREAD = item.dog.bread
            AppConstants.USER_NEUTERED = item.dog.neutered
            AppConstants.USER_SEX = item.dog.sex
            AppConstants.USER_SIZE = item.dog.size
            AppConstants.USER_NOT_LIKE = item.dog.notLike
            AppConstants.PROFILE_PICTURE_PATH = item.dog.profilePicturePath.toString()
            AppConstants.USER_ID = item.userId


            findNavController().navigate(
                MatchMeFragmentDirections.actionMatchMeToMatchProfile())
        }
    }
}