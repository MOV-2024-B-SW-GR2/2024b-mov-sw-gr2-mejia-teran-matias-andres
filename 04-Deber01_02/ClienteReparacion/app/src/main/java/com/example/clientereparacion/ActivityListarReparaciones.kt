package com.example.clientereparacion

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.clientereparacion.modelo.Reparacion
import com.google.android.material.snackbar.Snackbar

class ActivityListarReparaciones : AppCompatActivity() {
    var reparaciones = ArrayList<Reparacion>()
    private lateinit var listViewReparaciones: ListView
    private lateinit var btnAgregarReparacion: Button
    private lateinit var controlador: Controlador
    private var reparacionSeleccionada: Reparacion? = null
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
        setContentView(R.layout.activity_listar_reparaciones)
        controlador = Controlador(this)
        listViewReparaciones = findViewById(R.id.listViewReparaciones)
        btnAgregarReparacion = findViewById(R.id.btn_agregar_reparacion)
        val btnIrAOtraActividad = findViewById<Button>(R.id.btn_volver_a_cliente)

        clienteSeleccionado = intent.getStringExtra("clienteId")?.toInt()

        btnAgregarReparacion.setOnClickListener {
            val intent = Intent(this, ActivityAgregarReparacion::class.java)
            intent.putExtra("clienteId", clienteSeleccionado.toString())
            startActivity(intent)
        }
        registerForContextMenu(listViewReparaciones)
        actualizarLista()

        btnIrAOtraActividad.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Reemplaza "OtraActividad" con la actividad deseada
            //intent.putExtra("clienteId", clienteSeleccionado?.toString()) // Si necesitas pasar datos, agrégalos aquí
            startActivity(intent)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_reparaciones, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as? AdapterView.AdapterContextMenuInfo
        val position = info?.position
        reparacionSeleccionada = position?.let { controlador.listarReparacionesPorCliente(clienteSeleccionado!!)[it] }
        when (item.itemId) {
            R.id.context_menu_editar_reparacion -> {
                val intent = Intent(this, ActivityAgregarReparacion::class.java)
                intent.putExtra("reparacionId", reparacionSeleccionada!!.id.toString())
                intent.putExtra("clienteId", clienteSeleccionado.toString())
                startActivity(intent)
            }
            R.id.context_menu_eliminar_reparacion -> {
                val respuesta = controlador.eliminarReparacion(reparacionSeleccionada!!.id)
                if (respuesta) {
                    mostrarSnackbar("Reparación eliminada")
                    actualizarLista()
                } else {
                    mostrarSnackbar("Error al eliminar reparación")
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun actualizarLista() {
        reparaciones = controlador.listarReparacionesPorCliente(clienteSeleccionado!!) as ArrayList<Reparacion>
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reparaciones.map { it.descripcion })
        listViewReparaciones.adapter = adapter
        listViewReparaciones.setOnItemClickListener { _, _, position, _ ->
            reparacionSeleccionada = reparaciones[position]
        }
    }
}
