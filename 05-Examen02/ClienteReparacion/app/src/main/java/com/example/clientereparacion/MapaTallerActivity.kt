package com.example.clientereparacion

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clientereparacion.modelo.Taller
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Button

class MapaTallerActivity : AppCompatActivity(), OnMapReadyCallback {
    private var taller: Taller? = null
    private var mapa: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_taller)

        // Configurar botón volver
        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish() // Cierra esta actividad y vuelve a la anterior
        }

        // Obtener el taller
        taller = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("taller", Taller::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("taller")
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap

        taller?.let { tallerNoNulo ->
            val posicion = LatLng(tallerNoNulo.latitud, tallerNoNulo.longitud)
            
            mapa?.apply {
                // Configuración básica del mapa
                uiSettings.isZoomControlsEnabled = true
                
                // Agregar marcador
                addMarker(
                    MarkerOptions()
                        .position(posicion)
                        .title(tallerNoNulo.nombre)
                )

                // Mover cámara
                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(posicion, 15f)
                )
            }
        }
    }
}
