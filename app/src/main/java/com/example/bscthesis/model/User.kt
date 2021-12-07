package com.example.bscthesis.model

data class User(val name: String,
                val bread: String,
                val sex : String,
                val age: String,
                val size: String,
                val neutered: String,
                val notLike: String,
                val beHereFor: String,
                val profilePicturePath : String?){
    constructor(): this(name="", bread="", sex="", age="", size="", neutered="", notLike="", beHereFor="", profilePicturePath=null)

}
