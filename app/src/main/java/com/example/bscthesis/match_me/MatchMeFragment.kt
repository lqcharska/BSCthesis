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
import com.example.bscthesis.R
import com.example.bscthesis.databinding.DogsItemBinding
import com.example.bscthesis.databinding.MatchMeFragmentBinding
import com.example.bscthesis.login.LoginFragmentDirections
import com.example.bscthesis.util.FirestoreUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
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

//        binding.matchMeReturnButton.setOnClickListener {
//            findNavController().navigate(
//                MatchMeFragmentDirections.actionMatchMeToMain()
//            )
//        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecycleView(items: List<BindableItem<DogsItemBinding>>){
        fun init(){
            Log.d("CHUJ", "jestem w updateRecycleView init()")
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MatchMeFragment.context)
                adapter = GroupieAdapter().apply {
                    dogSection = Section(items)
                    add(dogSection)

                }
            }
            shouldInitRecyclerView = false

        }

        fun updateItems(){

        }

        if (shouldInitRecyclerView) init()
        else updateItems()
    }
}