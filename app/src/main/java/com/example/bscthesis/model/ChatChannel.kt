package com.example.bscthesis.model

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}