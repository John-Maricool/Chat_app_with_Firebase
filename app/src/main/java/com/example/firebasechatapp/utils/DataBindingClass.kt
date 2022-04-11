package com.example.firebasechatapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatapp.data.adapter.ChatsListAdapter
import com.example.firebasechatapp.data.adapter.UsersListAdapter
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.data.models.UserInfo

@BindingAdapter("set_image_resource")
fun ImageView.setImageResource(uri: String?): String? {
    return if (uri != null) {
        Glide.with(context)
            .load(uri)
            .circleCrop()
            .into(this)
        uri
    } else {
        null
    }
}

@BindingAdapter("submitList")
fun submitList(recyclerView: RecyclerView, list: List<UserInfo>?) {
    val adapter = recyclerView.adapter as UsersListAdapter
    if (list != null) {
        adapter.getChats(list)
    }
}

@BindingAdapter("setAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: UsersListAdapter
) {
    adapter.let {
        recyclerView.adapter = it
    }
}


@BindingAdapter("submitChatsList")
fun submitChatsList(recyclerView: RecyclerView, list: List<ChatWithUserInfo>?) {
    val adapter = recyclerView.adapter as ChatsListAdapter
    if(list != null)
    adapter.getChats(list)
}

@BindingAdapter("setChatsAdapter")
fun setChatsAdapter(
    recyclerView: RecyclerView,
    adapter: ChatsListAdapter
) {
    adapter.let {
        recyclerView.adapter = it
    }
}