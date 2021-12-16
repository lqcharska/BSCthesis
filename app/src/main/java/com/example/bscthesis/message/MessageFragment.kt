package com.example.bscthesis.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bscthesis.AppConstants
import com.example.bscthesis.R
import com.example.bscthesis.databinding.MessageFragmentBinding
import com.example.bscthesis.databinding.TextMessageItemBinding
import com.example.bscthesis.model.MessageType
import com.example.bscthesis.model.TextMessage
import com.example.bscthesis.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

class MessageFragment : Fragment() {
    private lateinit var binding: MessageFragmentBinding

    private lateinit var messagesListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.message_fragment,
            container,
            false
        )

        binding.topAppBar.title = AppConstants.USER_NAME

        //get chat channel
        val otherUserId = AppConstants.USER_ID
        FirestoreUtil.getOrCreateChatChannel(otherUserId){ channelId ->
            messagesListenerRegistration = FirestoreUtil.addChatMessagesListener(channelId, this.requireActivity(), this::updateRecyclerView)

            binding.sendMessageButton.setOnClickListener {
                val messageToSend = TextMessage(binding.textMessageText.text.toString(), Calendar.getInstance().time, FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                binding.textMessageText.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

            binding.sendPhotoMessage.setOnClickListener {
                
            }

            binding.topAppBar.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.dog_profile ->{
                        findNavController().navigate(
                            MessageFragmentDirections.actionMessageToMessageProfile())

                        shouldInitRecyclerView = true

                        true
                    }
                    else -> false
                }
            }

        }

        return binding.root
    }

    private fun updateRecyclerView(messages: List<BindableItem<TextMessageItemBinding>>){
        fun init(){
            binding.recyclerViewMessage.apply{
                layoutManager = LinearLayoutManager(this@MessageFragment.context)
                adapter = GroupieAdapter().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false

        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView){
            init()
        } else {
            updateItems()
        }

        binding.recyclerViewMessage.scrollToPosition((binding.recyclerViewMessage.adapter?.itemCount
            ?: 1) - 1)

    }

}