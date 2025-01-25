package com.example.clientereparacion

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.clientereparacion.modelo.Cliente
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityAgregarCliente : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private var clienteSeleccionado: Int? = null

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
        setContentView(R.layout.activity_agregar_cliente)
        controlador = Controlador(this)

        val nombre = findViewById<EditText>(R.id.nombre_in)
        val telefono = findViewById<EditText>(R.id.et_id)
        val fechaRegistro = findViewById<EditText>(R.id.fecha_registro_in)
        val esMiembro = findViewById<CheckBox>(R.id.cb_es_miembro)
        val btnGuardarCliente = findViewById<Button>(R.id.btn_guardar_cliente)

        clienteSeleccionado = intent.getStringExtra("clienteId")?.toIntOrNull()
        clienteSeleccionado?.let { id ->
            val cliente = controlador.listarClientes().find { it.id == id }
            cliente?.let {
                nombre.setText(it.nombre)
                telefono.setText(it.telefono)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                fechaRegistro.setText(sdf.format(it.fechaRegistro))
                esMiembro.isChecked = it.esMiembro
            }
        }

        btnGuardarCliente.setOnClickListener {
            val nombreText = nombre.text.toString()
            val telefonoText = telefono.text.toString()
            val fechaRegistroText = fechaRegistro.text.toString()
            val esMiembroChecked = esMiembro.isChecked

            if (nombreText.isNotBlank() && fechaRegistroText.isNotBlank() && telefonoText.isNotBlank()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaRegistroDate = sdf.parse(fechaRegistroText) ?: Date()

                if (clienteSeleccionado != null) {
                    controlador.actualizarCliente(
                        Cliente(clienteSeleccionado!!, nombreText, fechaRegistroDate, telefonoText, esMiembroChecked)
                    )
                    mostrarSnackbar("Cliente actualizado")
                } else {
                    controlador.crearCliente(
                        Cliente(nombreText, fechaRegistroDate, telefonoText, esMiembroChecked)
                    )
                    mostrarSnackbar("Cliente creado")
                }
                finish()
            } else {
                mostrarSnackbar("Todos los campos son obligatorios")
            }
        }
    }
}
