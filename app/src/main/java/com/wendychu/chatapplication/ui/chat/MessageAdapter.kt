package com.wendychu.chatapplication.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wendychu.chatapplication.databinding.ItemReceivedMsgBinding
import com.wendychu.chatapplication.databinding.ItemSentMsgBinding
import com.wendychu.chatapplication.model.Message

class MessageAdapter(
    private val context: Context,
    private val msgList: ArrayList<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    enum class MSG_TYPE{
        RECEIVE,
        SENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MSG_TYPE.RECEIVE.ordinal){
            ReceivedViewHolder(ItemReceivedMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            SentViewHolder(ItemSentMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java){
            msgList[position].message?.let {
                val viewHolder = holder as SentViewHolder
                viewHolder.bind(it)
            }
        } else {
            msgList[position].message?.let {
                val viewHolder = holder as ReceivedViewHolder
                viewHolder.bind(it)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMsg = msgList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMsg.senderId)){
            MSG_TYPE.SENT.ordinal
        } else {
            MSG_TYPE.RECEIVE.ordinal
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    inner class SentViewHolder(private val sentView: ItemSentMsgBinding) : RecyclerView.ViewHolder(sentView.root){
        fun bind(msg: String){
            sentView.tvSentMsg.text = msg
        }
    }

    inner class ReceivedViewHolder(private val receivedView: ItemReceivedMsgBinding) : RecyclerView.ViewHolder(receivedView.root){
        fun bind(msg: String){
            receivedView.tvReceivedMsg.text = msg
        }
    }
}