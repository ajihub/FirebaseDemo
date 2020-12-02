package com.example.firebasedemo2.handlers

import com.example.firebasedemo2.models.Restaurant
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RestaurantHandler {
    var  database: FirebaseDatabase
    var restaurantRef: DatabaseReference

    init{
        database = FirebaseDatabase.getInstance()
        restaurantRef =  database.getReference("restaurants")
    }

    fun create(restaurant: Restaurant): Boolean{
        val id = restaurantRef.push().key
        restaurant.id = id

        restaurantRef.child(id!!).setValue(restaurant)
        return true
    }
    fun update(restaurant: Restaurant): Boolean{
        restaurantRef.child(restaurant.id!!).setValue(restaurant)
        return true
    }
    fun  delete(restaurant: Restaurant): Boolean{
        restaurantRef.child(restaurant.id!!).removeValue()
        return true
    }
}