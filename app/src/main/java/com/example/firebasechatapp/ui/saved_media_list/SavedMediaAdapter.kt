package com.example.firebasechatapp.ui.saved_media_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.interfaces.OnSavedMediaItemClickedListener
import com.example.firebasechatapp.databinding.SavedMediaListItemBinding
import javax.inject.Inject

class SavedMediaAdapter
@Inject constructor() :
    RecyclerView.Adapter<SavedMediaAdapter.SavedMediaViewHolder>() {

    var medias: List<String> = listOf()
    lateinit var listener: OnSavedMediaItemClickedListener

    inner class SavedMediaViewHolder(val binding: SavedMediaListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.media = item
            binding.listener = listener
        }
    }

    fun setOnItemClickListener(mListener: OnSavedMediaItemClickedListener) {
        listener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMediaViewHolder {
        val binding = DataBindingUtil.inflate<SavedMediaListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.saved_media_list_item,
            parent,
            false
        )
        return SavedMediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedMediaViewHolder, position: Int) {
        holder.bind(medias[position])
    }

    fun getMedias(mMedia: List<String>) {
        medias = mMedia
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return medias.size
    }

}