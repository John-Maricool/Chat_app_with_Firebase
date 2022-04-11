package com.example.firebasechatapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.interfaces.OnListItemClickListener
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.databinding.ListItemUserBinding
import javax.inject.Inject

class UsersListAdapter
@Inject constructor() :
    RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder>() {

    var chats: List<UserInfo> = listOf()
    lateinit var listener: OnListItemClickListener

    inner class UsersListViewHolder(val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserInfo){
            binding.chatwithuserinfo = item
            binding.listener = listener
        }
    }

    fun setOnItemClickListener(mListener: OnListItemClickListener){
        listener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        val binding = DataBindingUtil.inflate<ListItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_user,
            parent,
            false
        )
        return UsersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    fun getChats(mChats: List<UserInfo>) {
        chats = mChats
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}