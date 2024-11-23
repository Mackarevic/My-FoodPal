package com.example.myfoodpal.model

data class FoodItem(
    val description: String,  // Beschreibung des Lebensmittels
    val foodNutrients: List<Nutrient>  // Liste der Nährstoffe
)

data class Nutrient(
    val nutrientName: String, // Name des Nährstoffs, z. B. "Energy"
    val value: Float          // Wert des Nährstoffs, z. B. Kalorien
)

