package com.example.bscthesis.recycleview.item

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.bscthesis.R
import com.example.bscthesis.databinding.ImageMessageItemBinding
import com.example.bscthesis.databinding.TextMessageItemBinding
import com.example.bscthesis.glide.GlideApp
import com.example.bscthesis.model.ImageMessage
import com.example.bscthesis.model.TextMessage
import com.example.bscthesis.util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import java.text.SimpleDateFormat

class ImageMessageItem(val message: ImageMessage, val context: Context): BindableItem<ImageMessageItemBinding>() {
    override fun bind(binding: ImageMessageItemBinding, position: Int) {
        GlideApp.with(context)
            .load(StorageUtil.pathToReference(message.imagePath))
            .placeholder(R.drawable.send_image_message)
            .into(binding.imageMessageImageView)
        setTimeText(binding)
        setMessageRootGravity(binding)
    }

    override fun getLayout(): Int {
        return R.layout.image_message_item
    }

    override fun isSameAs(other: Item<*>): Boolean {
        if (other !is ImageMessageItem){
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

    override fun initializeViewBinding(view: View): ImageMessageItemBinding {
        return ImageMessageItemBinding.bind(view)
    }

    private fun setTimeText(binding: ImageMessageItemBinding){
        val dateFormat = SimpleDateFormat.
        getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

        binding.timestampText.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(binding: ImageMessageItemBinding){
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

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}