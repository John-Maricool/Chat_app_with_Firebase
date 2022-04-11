package com.example.firebasechatapp.ui.chat

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.adapter.ChatMessagesAdapter
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.utils.Constants
import java.text.SimpleDateFormat

@BindingAdapter("setMessageText")
fun setMessageText(view: TextView, message: Message){
    if (message.type == Constants.TYPE_TEXT){
      view.text = message.text
    }
}

@BindingAdapter("setImageRes")
fun setImageRes(view: ImageView, message: Message){
    if (message.type == Constants.TYPE_IMAGE){
       Glide.with(view.context)
           .load(message.text)
           .into(view)
    }
}

@BindingAdapter("setVideoRes")
fun setVideoRes(view: VideoView, message: Message){
    if (message.type == Constants.TYPE_VIDEO){
        view.setVideoPath(message.text)
    }
}

@BindingAdapter("setMessageSeen")
fun setMessageSeenText(view: TextView, message: Message){
    if (message.seen == true){
        view.text = "seen"
    }else{
        view.text = "Not seen"
    }
}

@BindingAdapter("setMessageTime")
fun setMessageTime(view: TextView, message: Message){
    val dateFormat =
        SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
    view.text = dateFormat.format(message.sentTime)
}

@BindingAdapter("submitChatMessages")
fun submitList(recyclerView: RecyclerView, list: List<Message>?) {
    val adapter = recyclerView.adapter as ChatMessagesAdapter
    if (list != null) {
        adapter.getMessages(list)
        recyclerView.scrollToPosition(list.size - 1)
    }
}

@BindingAdapter("setMessagesAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: ChatMessagesAdapter
) {
    adapter.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("setUserInfoName")
fun setUserInfoName(view: TextView, info: String?){
    if (info != null)
    view.text = info
}

@BindingAdapter("toggleViewVisibility")
fun changeVisibility(view: Group, value: Boolean){
    if (value){
        view.visibility = View.VISIBLE
        view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.view_show))
    }
    else{
        view.visibility = View.GONE
        view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.view_hide))
    }
}


