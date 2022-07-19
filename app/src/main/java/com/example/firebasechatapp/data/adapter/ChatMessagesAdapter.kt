package com.example.firebasechatapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.interfaces.ConstraintInstructions
import com.example.firebasechatapp.data.interfaces.OnMediaItemClickListener
import com.example.firebasechatapp.data.models.Message
import com.example.firebasechatapp.databinding.ChatItemLeftBinding
import com.example.firebasechatapp.databinding.ChatItemRightBinding
import com.example.firebasechatapp.utils.Constants
import com.example.firebasechatapp.utils.SharedPrefsCalls
import javax.inject.Inject

class ChatMessagesAdapter
@Inject constructor(val prefs: SharedPrefsCalls) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val USER_MAIN = 1
    private val USER_OTHER = 2

    lateinit var listener: OnMediaItemClickListener

    var messages = mutableListOf<Message>()

    inner class ChatMessageViewHolderLeft(val binding: ChatItemLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Message) {
            when (data.type) {
                Constants.TYPE_TEXT -> {
                    val inst = ConstraintInstructions.ConnectConstraint(
                        R.id.message_time,
                        ConstraintSet.TOP,
                        R.id.show_message,
                        ConstraintSet.BOTTOM
                    )
                    binding.constInside.updateConstraints(inst)
                }

                else -> {
                    val inst = ConstraintInstructions.ConnectConstraint(
                        R.id.message_time,
                        ConstraintSet.TOP,
                        R.id.image,
                        ConstraintSet.BOTTOM
                    )
                    binding.constInside.updateConstraints(inst)
                }
            }
            binding.message = data
            binding.listener = listener
        }
    }

    inner class ChatMessageViewHolderRight(val binding: ChatItemRightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            when (data.type) {
                Constants.TYPE_TEXT -> {
                    val inst = ConstraintInstructions.ConnectConstraint(
                        R.id.message_time,
                        ConstraintSet.TOP,
                        R.id.show_message,
                        ConstraintSet.BOTTOM
                    )
                    binding.constInside.updateConstraints(inst)

                }
                else -> {
                    val inst = ConstraintInstructions.ConnectConstraint(
                        R.id.message_time,
                        ConstraintSet.TOP,
                        R.id.image,
                        ConstraintSet.BOTTOM
                    )
                    binding.constInside.updateConstraints(inst)
                }
            }
            binding.message = data
            binding.listener = listener
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == USER_OTHER) {
            val binding = ChatItemLeftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ChatMessageViewHolderLeft(binding)
        } else {
            val binding = ChatItemRightBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ChatMessageViewHolderRight(binding)
        }
    }

    fun setOnItemClickListener(mListener: OnMediaItemClickListener) {
        listener = mListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == prefs.getUserUid()) {
            USER_MAIN
        } else {
            USER_OTHER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = messages[position]
        if (holder is ChatMessageViewHolderRight) {
            holder.bind(data)
        } else if (holder is ChatMessageViewHolderLeft) {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun getMessages(mMessages: List<Message>) {
        messages = mMessages as MutableList<Message>
        notifyDataSetChanged()
    }

    fun addNewMessages(mMessages: List<Message>) {
        val diffCallback = CoursesCallback(messages, mMessages)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        messages.clear()
        messages = mMessages as MutableList<Message>

        diffCourses.dispatchUpdatesTo(this)
        // notifyItemRangeChanged(0, messages.size)
        notifyItemInserted(0)

    }
}

class CoursesCallback(private val oldList: List<Message>, private val newList: List<Message>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].text === newList[newItemPosition].text && oldList[oldItemPosition].sentTime == newList[newItemPosition].sentTime
    }

    override fun areContentsTheSame(oldCourse: Int, newPosition: Int): Boolean {
        return oldList[oldCourse].text === newList[newPosition].text && oldList[oldCourse].sentTime == newList[newPosition].sentTime
    }
}

fun ConstraintLayout.updateConstraints(instruction: ConstraintInstructions) {
    ConstraintSet().also {
        it.clone(this)
        if (instruction is ConstraintInstructions.ConnectConstraint) it.connect(
            instruction.startID,
            instruction.startSide,
            instruction.endID,
            instruction.endSide
        )
        if (instruction is ConstraintInstructions.DisconnectConstraint) it.clear(
            instruction.startID,
            instruction.startSide
        )
        it.applyTo(this)
    }
}