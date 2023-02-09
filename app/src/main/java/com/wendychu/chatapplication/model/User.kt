package com.wendychu.chatapplication.model

import java.io.Serializable


data class User(
    val name: String = "",
    val email: String = "",
    val uid: String = ""
) : Serializable
