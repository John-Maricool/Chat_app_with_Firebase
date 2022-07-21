package com.example.firebasechatapp.utils

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechatapp.R
import com.example.firebasechatapp.cache_source.UserEntity
import com.example.firebasechatapp.data.adapter.ChatsListAdapter
import com.example.firebasechatapp.data.adapter.SavedMediaAdapter
import com.example.firebasechatapp.data.adapter.UsersListAdapter
import com.example.firebasechatapp.data.models.UserInfo
import com.google.android.material.tabs.TabLayout

@BindingAdapter("set_image_resource")
fun ImageView.setImageResource(uri: String?): String? {
    return if (uri != null) {
        Glide.with(context)
            .load(uri)
            .placeholder(R.drawable.ic_account_circle)
            .error(R.drawable.ic_account_circle)
            .circleCrop()
            .into(this)
        uri
    } else {
        null
    }
}

@BindingAdapter("set_image_resource_normal")
fun ImageView.setImageResourceNormal(uri: Bitmap) {
    Glide.with(context)
        .load(uri)
        .placeholder(R.drawable.ic_account_circle)
        .error(R.drawable.ic_account_circle)
        .into(this)
}


@BindingAdapter("set_image_media")
fun ImageView.setImageMedia(uri: String) {
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .placeholder(R.drawable.ic_media)
        .error(R.drawable.ic_media)
        .into(this)
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
fun submitChatsList(recyclerView: RecyclerView, list: List<UserEntity>?) {
    val adapter = recyclerView.adapter as ChatsListAdapter
    if (list != null)
        adapter.getChats(list)
}


@BindingAdapter("setMediaAdapter")
fun setMediaAdapter(
    recyclerView: RecyclerView,
    adapter: SavedMediaAdapter
) {
    recyclerView.setHasFixedSize(true)
    val lm = GridLayoutManager(recyclerView.context, 4)
    recyclerView.layoutManager = lm
    adapter.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("submitSavedMediaList")
fun submitMediaList(recyclerView: RecyclerView, list: List<String>?) {
    val adapter = recyclerView.adapter as SavedMediaAdapter
    if (list != null)
        adapter.getMedias(list)
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


@BindingAdapter("set_text")
fun setText(view: TextView, time: Long) {
    view.text = time.toTimeAgo()
}


@BindingAdapter("set_last_message_text")
fun setLastMessageText(view: TextView, user: UserEntity) {
    when (user.type) {
        Constants.TYPE_TEXT -> {
            view.text = user.lastMessage
        }
        Constants.TYPE_IMAGE -> {
            view.text = "You have an Image"
        }
        else -> {
            view.text = "You have a video"
        }
    }
}


@BindingAdapter("set_type", "set_media_uri", requireAll = true)
fun ImageView.fillImageView(type: Int, uri: String) {
    if (type == Constants.TYPE_IMAGE) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.pic_placeholder)
            .into(this)
    } else {
        visibility = View.GONE
    }
}


@BindingAdapter("set_type", "set_media_uri", requireAll = true)
fun VideoView.fillVideoView(type: Int, uri: String) {
    if (type == Constants.TYPE_VIDEO) {
        val urI = Uri.parse(uri)
        setVideoURI(urI)
        val controller = MediaController(context)
        controller.setAnchorView(this)
        controller.setMediaPlayer(this)
        this.setMediaController(controller)
        this.start()
    } else {
        visibility = View.GONE
    }
}


@BindingAdapter("set_tab_text_video")
fun TabLayout.setTabText(text: String) {
    val oneTab: TabLayout.Tab = newTab()
    oneTab.text = text
    addTab(oneTab)
}


@BindingAdapter("set_tab_text_image")
fun TabLayout.setTabTextImg(text: String) {
    val oneTab: TabLayout.Tab = newTab()
    oneTab.text = text
    addTab(oneTab)
}



