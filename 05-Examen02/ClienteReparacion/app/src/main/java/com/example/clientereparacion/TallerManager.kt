package com.example.clientereparacion

import com.example.clientereparacion.modelo.Taller

object TallerManager {
    private val talleres = listOf(
        // Coordenadas actualizadas para ubicaciones en Quito
        Taller(1, "Taller - Bici Outlet", -0.2047165888610578, -78.49233581927383),  // Cerca del parque El Ejido
        Taller(2, "Taller - Lobos Bike", -0.21104303577147118, -78.50066250104811),   // Cerca del parque La Carolina
        Taller(3, "Taller - BicisEc", -0.210721172876048, -78.49948232921139)          // Sector de Solanda
    )

    fun obtenerTalleres(): List<Taller> = talleres

    fun obtenerTallerPorId(id: Int): Taller? = talleres.find { it.id == id }
}
