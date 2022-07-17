package com.example.firebasechatapp.ui.chat

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.adapter.ChatMessagesAdapter
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.utils.Constants
import java.text.SimpleDateFormat


@BindingAdapter("setMessageText")
fun setMessageText(view: TextView, message: Message) {
    if (message.type == Constants.TYPE_TEXT) {
        view.text = message.text
    }
}

@BindingAdapter("setImageRes")
fun setImageRes(view: ImageView, message: Message) {
    if (message.type == Constants.TYPE_IMAGE || message.type == Constants.TYPE_VIDEO) {
        Glide.with(view.context)
            .load(message.text)
            .placeholder(R.drawable.blurry)
            .into(view)
    }
}

@BindingAdapter("setVideoRes")
fun setVideoRes(view: ImageView, message: Message) {
    if (message.type == Constants.TYPE_VIDEO) {
        Glide.with(view.context)
            .load(message.text)
            .placeholder(R.drawable.blurry)
            .into(view)
    }
}

@BindingAdapter("setMessageSeen")
fun ImageView.setMessageSeenText(message: Message) {
    if (message.seen == true) {
        setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_check_seen, null))
    } else {
        setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_check_not_seen, null))
    }
}

@BindingAdapter("setMessageTime")
fun setMessageTime(view: TextView, message: Message) {
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
   /* val animator = recyclerView.itemAnimator
    if (animator is SimpleItemAnimator) {
        animator.supportsChangeAnimations = false
        animator.endAnimations()
    }

*/
    recyclerView.itemAnimator = null
    adapter.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("setUserInfoName")
fun setUserInfoName(view: TextView, info: String?) {
    if (info != null)
        view.text = info
}

@BindingAdapter("toggleViewVisibility")
fun changeVisibility(view: Group, value: Boolean) {
    if (value) {
        view.visibility = View.VISIBLE
        view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.view_show))
    } else {
        view.visibility = View.GONE
        view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.view_hide))
    }
}


