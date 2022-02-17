package com.example.firebasechatapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.databinding.ListItemChatBinding
import com.example.firebasechatapp.ui.chats.ChatsViewModel

class ChatsListAdapter(var viewModel: ChatsViewModel):
    RecyclerView.Adapter<ChatsListAdapter.ChatsListViewHolder>() {

    var chats: List<UserInfo> = listOf()
    var lastMessage: List<Message> = listOf()

    inner class ChatsListViewHolder(val binding: ListItemChatBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: UserInfo, message: Message, model: ChatsViewModel){
                binding.chatwithuserinfo = item
                binding.viewmodel = model
                binding.message = message
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemChatBinding.inflate(layoutInflater, parent, false)
        return ChatsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatsListViewHolder, position: Int) {
        holder.bind(chats[position], lastMessage[position], viewModel)
    }

    fun getChats(mChats: List<UserInfo>, mMessage: List<Message>){
        chats = mChats
        lastMessage = mMessage
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return chats.size
    }

    fun updateEmployeeListItems(infos: List<UserInfo>) {
        val diffCallback: DiffUtil.Callback = ChatDiffCallback(infos)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.chats.toMutableList().clear()
        this.chats.toMutableList().addAll(infos)
        diffResult.dispatchUpdatesTo(this)
    }
}


class ChatDiffCallback(var info: List<UserInfo>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        TODO("Not yet implemented")
    }

    override fun getNewListSize(): Int {
        return info.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return info[oldItemPosition] == info[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return info[oldItemPosition].id == info[newItemPosition].id
    }

}

