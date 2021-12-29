package com.example.bscthesis.message

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bscthesis.AppConstants
import com.example.bscthesis.R
import com.example.bscthesis.databinding.ImageMessageItemBinding
import com.example.bscthesis.databinding.MessageFragmentBinding
import com.example.bscthesis.databinding.TextMessageItemBinding
import com.example.bscthesis.model.ImageMessage
import com.example.bscthesis.model.MessageType
import com.example.bscthesis.model.TextMessage
import com.example.bscthesis.util.FirestoreUtil
import com.example.bscthesis.util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import java.io.ByteArrayOutputStream
import java.util.*

class MessageFragment : Fragment() {
    private lateinit var binding: MessageFragmentBinding

    private lateinit var messagesListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    private lateinit var currentChannelId: String

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

        binding.topAppBar.title = AppConstants.USER_NAME.uppercase()

        //get chat channel
        val otherUserId = AppConstants.USER_ID
        FirestoreUtil.getOrCreateChatChannel(otherUserId){ channelId ->
            currentChannelId = channelId
            messagesListenerRegistration = FirestoreUtil.addChatMessagesListener(channelId, this.requireActivity(), this::updateRecyclerView)

            binding.sendMessageButton.setOnClickListener {
                val messageToSend = TextMessage(binding.textMessageText.text.toString(), Calendar.getInstance().time, FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                binding.textMessageText.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

//            binding.sendPhotoMessage.setOnClickListener {
//                val intent = Intent().apply{
//                    type = "image/*"
//                    action = Intent.ACTION_GET_CONTENT
//                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
//                }
//                startActivityForResult(Intent.createChooser(intent, "Select image"), RC_SELECT_IMAGE)
//
//            }

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




//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
//            val selectedImagePath =data.data
//            val selectedImageBmp = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)
//
//            val outputStream = ByteArrayOutputStream()
//            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
//            val selectedImageBytes = outputStream.toByteArray()
//
//            StorageUtil.uploadImageMessage(selectedImageBytes){ imagePath ->
//                val messageToSend =
//                    ImageMessage(imagePath, Calendar.getInstance().time, FirebaseAuth.getInstance().currentUser!!.uid)
//                FirestoreUtil.sendMessage(messageToSend, currentChannelId)
//            }
//
//        }
//    }

}