package com.wendychu.chatapplication.ui.chat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wendychu.chatapplication.R
import com.wendychu.chatapplication.databinding.FragmentChatBinding
import com.wendychu.chatapplication.model.Message

class ChatFragment : Fragment(R.layout.fragment_chat) {
    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private var msgList = arrayListOf<Message>()
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentChatBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference
        adapter = MessageAdapter(this.requireContext(), msgList)
        binding.rvChat.adapter = adapter

        val receiver = arguments?.getString("user")
        val receiverUid = arguments?.getString("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        (activity as AppCompatActivity).supportActionBar?.title = receiver

        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    msgList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        msgList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        binding.btnSend.setOnClickListener {
            val msg = binding.etMsgBox.text.toString()
            val msgObject = Message(msg, senderUid)

            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(msgObject)
                .addOnSuccessListener {
                    dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(msgObject)
                }
            binding.etMsgBox.text.clear()
        }
    }

}