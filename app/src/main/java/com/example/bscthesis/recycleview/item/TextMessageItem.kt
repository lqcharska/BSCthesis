package com.example.bscthesis.recycleview.item

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.bscthesis.R
import com.example.bscthesis.databinding.TextMessageItemBinding
import com.example.bscthesis.model.TextMessage
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import java.text.SimpleDateFormat

class TextMessageItem(val message: TextMessage, val context: Context): BindableItem<TextMessageItemBinding>() {
    override fun bind(binding: TextMessageItemBinding, position: Int) {
        binding.messageTextInput.text = message.text
        setTimeText(binding)
        setMessageRootGravity(binding)
    }

    override fun getLayout(): Int {
        return R.layout.text_message_item
    }

    override fun isSameAs(other: Item<*>): Boolean {
        if (other !is TextMessageItem){
            return false
        }
        if (this.message != other.message){
            return false
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as TextMessageItem)
    }

    override fun initializeViewBinding(view: View): TextMessageItemBinding {
        return TextMessageItemBinding.bind(view)
    }

    private fun setTimeText(binding: TextMessageItemBinding){
        val dateFormat = SimpleDateFormat.
        getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

        binding.timestampText.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(binding: TextMessageItemBinding){
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid){
            binding.relativeLayout.apply{
                setBackgroundResource(R.drawable.rect_message_me_field)
                val lParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END)
                this.layoutParams = lParams
            }
        } else {
            binding.relativeLayout.apply {
                setBackgroundResource(R.drawable.rect_message_other_dog_field)
                val lParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START)
                this.layoutParams = lParams
            }

        }
    }

}