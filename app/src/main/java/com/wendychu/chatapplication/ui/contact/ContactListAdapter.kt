package com.wendychu.chatapplication.ui.contact

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wendychu.chatapplication.databinding.ItemContactBinding
import com.wendychu.chatapplication.model.User

typealias UserItemClick = (item: User) -> Unit

class ContactListAdapter(
    private val contactList: List<User>,
    private val onItemClick: UserItemClick
) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactList[position], onItemClick)
    }

    override fun getItemCount(): Int = contactList.size

    inner class ViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, onItemClick: UserItemClick){
            binding.tvUsername.text = user.name
            binding.llItem.setOnClickListener{
                onItemClick(user)
            }
        }
    }

}