package com.example.clientereparacion

import android.content.ContentValues
import android.content.Context
import com.example.clientereparacion.modelo.Cliente
import com.example.clientereparacion.modelo.Reparacion
import java.util.Date

class Controlador(context: Context) {
    private val dbHelper = SQLiteHelper(context)

    // Crear un cliente
    fun crearCliente(cliente: Cliente): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("telefono", cliente.telefono) // Agregar teléfono
            put("fecha_registro", cliente.fechaRegistro.time)
            put("es_miembro", if (cliente.esMiembro) 1 else 0)
        }
        val resultado = db.insert("CLIENTE", null, valores)
        db.close()
        return resultado != -1L
    }

    fun obtenerClientePorId(clienteId: Int): Cliente? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM CLIENTE WHERE id = ?", arrayOf(clienteId.toString()))
        var cliente: Cliente? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")) // Agregar teléfono si es necesario
            val fechaRegistro = Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha_registro")))
            val esMiembro = cursor.getInt(cursor.getColumnIndexOrThrow("es_miembro")) > 0
            cliente = Cliente(id, nombre, fechaRegistro, telefono, esMiembro)
        }
        cursor.close()
        db.close()
        return cliente
    }

    // Listar clientes
    fun listarClientes(): List<Cliente> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM CLIENTE", null)
        val clientes = mutableListOf<Cliente>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")) // Agregar teléfono
            val fechaRegistro = Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha_registro")))
            val esMiembro = cursor.getInt(cursor.getColumnIndexOrThrow("es_miembro")) > 0
            clientes.add(Cliente(id, nombre, fechaRegistro, telefono, esMiembro))
        }
        cursor.close()
        db.close()
        return clientes
    }

    // Actualizar cliente
    fun actualizarCliente(cliente: Cliente): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("telefono", cliente.telefono) // Agregar teléfono
            put("fecha_registro", cliente.fechaRegistro.time)
            put("es_miembro", if (cliente.esMiembro) 1 else 0)
        }
        val rows = db.update("CLIENTE", valores, "id = ?", arrayOf(cliente.id.toString()))
        db.close()
        return rows > 0
    }

    // Eliminar cliente
    fun eliminarCliente(clienteId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val filas = db.delete("CLIENTE", "id = ?", arrayOf(clienteId.toString()))
        db.close()
        return filas > 0
    }

    // Crear una reparación
    fun crearReparacion(reparacion: Reparacion): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("descripcion", reparacion.descripcion)
            put("fecha_reparacion", reparacion.fechaReparacion.time)
            put("costo", reparacion.costo)
            put("garantia", if (reparacion.garantia) 1 else 0)
            put("cliente_id", reparacion.cliente_id)
        }
        val resultado = db.insert("REPARACION", null, valores)
        db.close()
        return resultado != -1L
    }

    // Listar reparaciones de un cliente
    fun listarReparacionesPorCliente(clienteId: Int): List<Reparacion> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM REPARACION WHERE cliente_id = ?", arrayOf(clienteId.toString()))
        val reparaciones = mutableListOf<Reparacion>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            val fechaReparacion = Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha_reparacion")))
            val costo = cursor.getDouble(cursor.getColumnIndexOrThrow("costo"))
            val garantia = cursor.getInt(cursor.getColumnIndexOrThrow("garantia")) > 0
            val clienteIdDesdeBD = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id"))
            reparaciones.add(Reparacion(id, descripcion, fechaReparacion, costo, garantia, clienteIdDesdeBD))
        }
        cursor.close()
        db.close()
        return reparaciones
    }

    // Actualizar reparación
    fun actualizarReparacion(reparacion: Reparacion): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("descripcion", reparacion.descripcion)
            put("fecha_reparacion", reparacion.fechaReparacion.time)
            put("costo", reparacion.costo)
            put("garantia", if (reparacion.garantia) 1 else 0)
        }
        val rows = db.update("REPARACION", valores, "id = ?", arrayOf(reparacion.id.toString()))
        db.close()
        return rows > 0
    }

    // Eliminar reparación
    fun eliminarReparacion(reparacionId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val filas = db.delete("REPARACION", "id = ?", arrayOf(reparacionId.toString()))
        db.close()
        return filas > 0
    }

    // Obtener reparación por ID
    fun obtenerReparacion(reparacionId: Int): Reparacion? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM REPARACION WHERE id = ?", arrayOf(reparacionId.toString()))
        var reparacion: Reparacion? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            val fechaReparacion = Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha_reparacion")))
            val costo = cursor.getDouble(cursor.getColumnIndexOrThrow("costo"))
            val garantia = cursor.getInt(cursor.getColumnIndexOrThrow("garantia")) > 0
            val clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id"))
            reparacion = Reparacion(id, descripcion, fechaReparacion, costo, garantia, clienteId)
        }
        cursor.close()
        db.close()
        return reparacion
    }
}
