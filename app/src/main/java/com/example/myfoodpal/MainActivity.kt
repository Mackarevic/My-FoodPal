package com.example.myfoodpal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : ComponentActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var calInput: EditText
    private lateinit var calButton: Button
    private lateinit var calView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calInput  =  findViewById(R.id.calInput)
        calButton =  findViewById(R.id.calButton)
        calView   =  findViewById(R.id.calView)

        database = FirebaseDatabase.getInstance().getReference("numbers")

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


    private fun addNumberToDatabase(number: Int) {
        val newEntry = database.push()
        newEntry.setValue(number)
            .addOnSuccessListener {
                calInput.text.clear()
                Toast.makeText(this, "Zahl erfolgreich hinzugefügt!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Fehler: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


