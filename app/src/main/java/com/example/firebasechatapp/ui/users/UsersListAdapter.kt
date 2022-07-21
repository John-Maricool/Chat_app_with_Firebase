package com.example.firebasechatapp.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.interfaces.OnListItemClickListener
import com.example.firebasechatapp.data.models.UserInfo
import com.example.firebasechatapp.databinding.ListItemUserBinding
import javax.inject.Inject

class UsersListAdapter
@Inject constructor() :
    RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder>(), Filterable {

    var chats: List<UserInfo> = listOf()
    var ListFiltered: MutableList<UserInfo> = mutableListOf()
    lateinit var listener: OnListItemClickListener

    inner class UsersListViewHolder(val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserInfo) {
            binding.chatwithuserinfo = item
            binding.listener = listener
        }
    }

    fun setOnItemClickListener(mListener: OnListItemClickListener) {
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
        holder.bind(ListFiltered[position])
    }

    fun getChats(mChats: List<UserInfo>) {
        chats = mChats
        ListFiltered = chats as MutableList<UserInfo>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return ListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                ListFiltered = if (charString.isEmpty()) chats as ArrayList<UserInfo> else {
                    val filteredList = ArrayList<UserInfo>()
                    chats
                        .filter {
                            (it.displayName.contains(constraint!!, true))
                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = ListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                ListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<UserInfo>
                notifyDataSetChanged()
            }
        }
    }
}