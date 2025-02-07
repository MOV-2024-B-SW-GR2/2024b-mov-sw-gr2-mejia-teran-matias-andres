package com.example.clientereparacion

import com.example.clientereparacion.modelo.Taller

object TallerManager {
    private val talleres = listOf(
        // Coordenadas actualizadas para ubicaciones en Quito
        Taller(1, "Taller - Bici Outlet", -0.20500949486295827, -78.49236198837622),  // Cerca del parque El Ejido
        Taller(2, "Taller - Lobos Bike", -0.2114550873226654, -78.50063165463244),   // Cerca del parque La Carolina
        Taller(3, "Taller - BicisEc", -0.21098854583224072, -78.4993631200241)          // Sector de Solanda
    )

    fun obtenerTalleres(): List<Taller> = talleres

    fun obtenerTallerPorId(id: Int): Taller? = talleres.find { it.id == id }
}
