package com.example.firebasechatapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.interfaces.OnListItemClickListener
import com.example.firebasechatapp.data.models.ChatWithUserInfo
import com.example.firebasechatapp.databinding.ListItemChatBinding
import javax.inject.Inject

class ChatsListAdapter
@Inject constructor() :
    RecyclerView.Adapter<ChatsListAdapter.ChatsListViewHolder>() {

    var chats: List<ChatWithUserInfo> = listOf()
    lateinit var listener: OnListItemClickListener

    inner class ChatsListViewHolder(val binding: ListItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatWithUserInfo) {
            binding.chatwithuserinfo = item
            binding.listener = listener
        }
    }

    fun setOnItemClickListener(mListener: OnListItemClickListener){
        listener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsListViewHolder {
        val binding = DataBindingUtil.inflate<ListItemChatBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_chat,
            parent,
            false
        )
        return ChatsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatsListViewHolder, position: Int){
        holder.bind(chats[position])
    }

    fun getChats(mChats: List<ChatWithUserInfo>) {
        chats = mChats
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}