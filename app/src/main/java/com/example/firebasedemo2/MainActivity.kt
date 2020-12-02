package com.example.firebasedemo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.firebasedemo2.handlers.RestaurantHandler
import com.example.firebasedemo2.models.Restaurant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var nameEditText: EditText
    lateinit var locationEditText: EditText
    lateinit var specialtyEditText: EditText
    lateinit var addEditButton: Button
    lateinit var restaurantHandler: RestaurantHandler
    lateinit var restaurants: ArrayList<Restaurant>
    lateinit var restaurantListView: ListView
    lateinit var restaurantGettingEdited: Restaurant

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        locationEditText = findViewById(R.id.locationEditText)
        specialtyEditText = findViewById(R.id.specialtyEditText)
        addEditButton = findViewById(R.id.addEditButton)
        restaurantHandler =  RestaurantHandler()

        restaurants = ArrayList()
        restaurantListView = findViewById(R.id.restaurantsListview)
        addEditButton.setOnClickListener{
            val name = nameEditText.text.toString()
            val location = locationEditText.text.toString()
            val specialty =  specialtyEditText.text.toString()
            if(addEditButton.text.toString()== "Add"){
                val restaurant = Restaurant(name = name, location = location, specialty = specialty)
                if(restaurantHandler.create(restaurant)){
                    Toast.makeText(applicationContext, "Restaurant added", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
            else if(addEditButton.text.toString() == "Update"){
                val restaurant = Restaurant (id = restaurantGettingEdited.id, name = name,  location = location, specialty = specialty)
                if(restaurantHandler.update(restaurant)){
                    Toast.makeText(applicationContext, "Restaurant updated", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }


        }
        registerForContextMenu(restaurantListView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.restaurant_options, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when(item.itemId){
            R.id.edit_restaurant ->{
                restaurantGettingEdited = restaurants[info.position]
                nameEditText.setText(restaurantGettingEdited.name)
                locationEditText.setText(restaurantGettingEdited.location)
                specialtyEditText.setText(restaurantGettingEdited.specialty)
                addEditButton.setText("Update")
                true
            }
            R.id.delete_restaurant ->{

                if(restaurantHandler.delete(restaurants[info.position])){
                    Toast.makeText(applicationContext, "Restaurant deleted", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else ->return super.onContextItemSelected(item)
        }

    }



    override fun onStart() {
        super.onStart()
        //register a listener to everytime the database updates
        restaurantHandler.restaurantRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                restaurants.clear()

                snapshot.children.forEach{
                        it -> val restaurant = it.getValue(Restaurant::class.java)
                        restaurants.add(restaurant!!)
                }
                restaurants.sortBy { it.name }
                val adapter =  ArrayAdapter<Restaurant>(applicationContext, android.R.layout.simple_list_item_1, restaurants)
                restaurantListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }


        })

    }
    fun clearFields(){
        nameEditText.text.clear()
        locationEditText.text.clear()
        specialtyEditText.text.clear()
        addEditButton.setText("Add")
    }
}