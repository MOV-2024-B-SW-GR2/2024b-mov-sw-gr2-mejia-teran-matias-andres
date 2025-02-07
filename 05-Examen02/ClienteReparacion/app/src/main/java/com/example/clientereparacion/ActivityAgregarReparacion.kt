package com.example.clientereparacion

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.clientereparacion.modelo.Reparacion
import com.example.clientereparacion.modelo.Taller
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityAgregarReparacion : AppCompatActivity() {
    private lateinit var btnGuardarReparacion: Button
    private lateinit var controlador: Controlador
    private var reparacionSeleccionada: Int? = null
    private var clienteSeleccionado: Int? = null
    private lateinit var spinnerTaller: Spinner
    private val talleres = TallerManager.obtenerTalleres()

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_reparacion)
        controlador = Controlador(this)
        btnGuardarReparacion = findViewById(R.id.btn_guardar_reparacion)
        spinnerTaller = findViewById(R.id.spinner_taller)

        val descripcion = findViewById<EditText>(R.id.descripcion_in)
        val fechaReparacion = findViewById<EditText>(R.id.fecha_reparacion_in)
        val costo = findViewById<EditText>(R.id.costo_in)
        val garantia = findViewById<CheckBox>(R.id.cb_garantia)
        clienteSeleccionado = intent.getStringExtra("clienteId")?.toIntOrNull()
        reparacionSeleccionada = intent.getStringExtra("reparacionId")?.toIntOrNull()

        // Configurar el spinner de talleres
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, talleres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTaller.adapter = adapter

        reparacionSeleccionada?.let {
            val reparacion = controlador.listarReparacionesPorCliente(clienteSeleccionado!!)
                .find { it.id == reparacionSeleccionada }
            if (reparacion != null) {
                descripcion.setText(reparacion.descripcion)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                fechaReparacion.setText(sdf.format(reparacion.fechaReparacion))
                costo.setText(reparacion.costo.toString())
                garantia.isChecked = reparacion.garantia
                // Seleccionar el taller correspondiente
                val tallerIndex = talleres.indexOfFirst { it.id == reparacion.taller.id }
                if (tallerIndex != -1) {
                    spinnerTaller.setSelection(tallerIndex)
                }
            }
        }

        btnGuardarReparacion.setOnClickListener {
            val descripcionText = descripcion.text.toString()
            val fechaReparacionText = fechaReparacion.text.toString()
            val costoText = costo.text.toString().toDoubleOrNull()
            val garantiaChecked = garantia.isChecked
            val tallerSeleccionado = spinnerTaller.selectedItem as Taller

            if (descripcionText.isNotBlank() && fechaReparacionText.isNotBlank() && costoText != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaReparacionDate = sdf.parse(fechaReparacionText) ?: Date()

                if (reparacionSeleccionada != null) {
                    controlador.actualizarReparacion(
                        Reparacion(
                            reparacionSeleccionada!!,
                            descripcionText,
                            fechaReparacionDate,
                            costoText,
                            garantiaChecked,
                            clienteSeleccionado!!,
                            tallerSeleccionado
                        )
                    )
                    mostrarSnackbar("Reparación actualizada")
                } else {
                    controlador.crearReparacion(
                        Reparacion(
                            descripcionText,
                            fechaReparacionDate,
                            costoText,
                            garantiaChecked,
                            clienteSeleccionado!!,
                            tallerSeleccionado
                        )
                    )
                    mostrarSnackbar("Reparación creada")
                }
                val intent = Intent(this, ActivityListarReparaciones::class.java)
                intent.putExtra("clienteId", clienteSeleccionado.toString())
                startActivity(intent)
            } else {
                mostrarSnackbar("Todos los campos son obligatorios")
            }
        }
    }
}
