package com.example.clientereparacion

import com.example.clientereparacion.modelo.Taller

object TallerManager {
    private val talleres = listOf(
        // Coordenadas actualizadas para ubicaciones en Quito
        Taller(1, "Taller Centro - La Mariscal", -0.2016400, -78.4889600),  // Cerca del parque El Ejido
        Taller(2, "Taller Norte - La Carolina", -0.1821500, -78.4843900),   // Cerca del parque La Carolina
        Taller(3, "Taller Sur - Solanda", -0.2627800, -78.5230700)          // Sector de Solanda
    )

    fun obtenerTalleres(): List<Taller> = talleres

    fun obtenerTallerPorId(id: Int): Taller? = talleres.find { it.id == id }
}
