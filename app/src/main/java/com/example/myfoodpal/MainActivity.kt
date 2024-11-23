package com.example.myfoodpal

import android.R.id.input
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myfoodpal.api.RetrofitInstance
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.myfoodpal.model.FoodResponse

class MainActivity : ComponentActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var calInput: EditText
    private lateinit var calButton: Button
    private lateinit var calView: TextView

    private val apiKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calInput = findViewById(R.id.calInput)
        calButton = findViewById(R.id.calButton)
        calView = findViewById(R.id.calView)

        val addFood = findViewById<Button>(R.id.addFood)

        database = FirebaseDatabase.getInstance().getReference("numbers")

        addFood.setOnClickListener {
            showFoodPopup(it)
        }

        calButton.setOnClickListener {
            val numberStr = calInput.text.toString()
            if (numberStr.isNotEmpty()) {
                val number = numberStr.toInt()
                addNumberToDatabase(number)
            } else {
                Toast.makeText(this, "Bitte eine Zahl eingeben", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showFoodPopup(anchor: android.view.View) {
        val popupMenu = PopupMenu(this, anchor)
        val api = RetrofitInstance.api
        //val inputAsString = input.bufferedReader().use { it.readText() }  // defaults to UTF-8

        // API-Aufruf für suchfunktion oder so i guess
        api.searchFoods(apiKey, "apple").enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foods = response.body()?.foods ?: return
                    foods.forEach { food ->
                        val description = food.description
                        val calories = food.foodNutrients.firstOrNull {
                            it.nutrientName == "Energy"
                        }?.value ?: 0f

                        // jedes Lebensmittel hinzufügen
                        popupMenu.menu.add(description).setOnMenuItemClickListener {
                            //  Auswahl eines Menüpunkts die Kalorien in Firebase ballern
                            saveToFirebase(description, calories)
                            Toast.makeText(
                                this@MainActivity,
                                "$description gespeichert!",
                                Toast.LENGTH_SHORT
                            ).show()
                            true
                        }
                    }
                    popupMenu.show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Fehler bei der API-Abfrage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fehler bei der Verbindung", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Funktion um die Daten nach Firebase zu schicken
    private fun saveToFirebase(description: String, calories: Float) {
        val database = FirebaseDatabase.getInstance().getReference("numbers")
        val foodId = database.push().key ?: return

        val data = mapOf(
            "description" to description,
            "calories" to calories
        )

        // Speichern in Firebase
        database.child(foodId).setValue(data)
    }

    // Funktion um eine Zahl in Datenbank zu speichern
    private fun addNumberToDatabase(number: Int) {
        val numberId = database.push().key ?: return

        val data = mapOf(
            "calories" to number
        )

        database.child(numberId).setValue(data)
    }
}
