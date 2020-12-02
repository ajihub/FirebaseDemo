package com.example.firebasedemo2.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Restaurant(var id: String? = "", var name: String? = "", var location: String? = "", var specialty: String? = "") {
    override fun toString(): String {
        return "$name with specialty in  $specialty at $location"
    }
}