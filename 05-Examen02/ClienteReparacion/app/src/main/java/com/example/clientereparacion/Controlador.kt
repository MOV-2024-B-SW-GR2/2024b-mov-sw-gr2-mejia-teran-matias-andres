package com.example.clientereparacion

import android.content.ContentValues
import android.content.Context
import com.example.clientereparacion.modelo.Cliente
import com.example.clientereparacion.modelo.Reparacion
import com.example.clientereparacion.modelo.Taller
import java.util.Date

class Controlador(context: Context) {
    private val dbHelper = SQLiteHelper(context)

    // Crear un cliente
    fun crearCliente(cliente: Cliente): Boolean {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", cliente.nombre)
            put("telefono", cliente.telefono)
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
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
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
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
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
            put("telefono", cliente.telefono)
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
            put("taller_id", reparacion.taller.id)
            put("taller_nombre", reparacion.taller.nombre)
            put("taller_latitud", reparacion.taller.latitud)
            put("taller_longitud", reparacion.taller.longitud)
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
            try {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
                val fechaReparacion = Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha_reparacion")))
                val costo = cursor.getDouble(cursor.getColumnIndexOrThrow("costo"))
                val garantia = cursor.getInt(cursor.getColumnIndexOrThrow("garantia")) > 0
                val clienteIdDesdeBD = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id"))
                
                // Intentar obtener datos del taller, usar valores por defecto si no existen
                val taller = try {
                    val tallerId = cursor.getInt(cursor.getColumnIndexOrThrow("taller_id"))
                    val tallerNombre = cursor.getString(cursor.getColumnIndexOrThrow("taller_nombre"))
                    val tallerLatitud = cursor.getDouble(cursor.getColumnIndexOrThrow("taller_latitud"))
                    val tallerLongitud = cursor.getDouble(cursor.getColumnIndexOrThrow("taller_longitud"))
                    Taller(tallerId, tallerNombre, tallerLatitud, tallerLongitud)
                } catch (e: Exception) {
                    // Si no encontramos las columnas del taller, usar el primer taller por defecto
                    TallerManager.obtenerTalleres().first()
                }

                reparaciones.add(Reparacion(id, descripcion, fechaReparacion, costo, garantia, clienteIdDesdeBD, taller))
            } catch (e: Exception) {
                // Si hay algún error al leer una reparación, la saltamos
                continue
            }
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
            try {
                put("taller_id", reparacion.taller.id)
                put("taller_nombre", reparacion.taller.nombre)
                put("taller_latitud", reparacion.taller.latitud)
                put("taller_longitud", reparacion.taller.longitud)
            } catch (e: Exception) {
                // Si no podemos actualizar los datos del taller, continuamos sin ellos
            }
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
            try {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
                val fechaReparacion = Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha_reparacion")))
                val costo = cursor.getDouble(cursor.getColumnIndexOrThrow("costo"))
                val garantia = cursor.getInt(cursor.getColumnIndexOrThrow("garantia")) > 0
                val clienteId = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id"))
                
                // Intentar obtener datos del taller, usar valores por defecto si no existen
                val taller = try {
                    val tallerId = cursor.getInt(cursor.getColumnIndexOrThrow("taller_id"))
                    val tallerNombre = cursor.getString(cursor.getColumnIndexOrThrow("taller_nombre"))
                    val tallerLatitud = cursor.getDouble(cursor.getColumnIndexOrThrow("taller_latitud"))
                    val tallerLongitud = cursor.getDouble(cursor.getColumnIndexOrThrow("taller_longitud"))
                    Taller(tallerId, tallerNombre, tallerLatitud, tallerLongitud)
                } catch (e: Exception) {
                    // Si no encontramos las columnas del taller, usar el primer taller por defecto
                    TallerManager.obtenerTalleres().first()
                }

                reparacion = Reparacion(id, descripcion, fechaReparacion, costo, garantia, clienteId, taller)
            } catch (e: Exception) {
                // Si hay algún error al leer la reparación, retornamos null
            }
        }
        cursor.close()
        db.close()
        return reparacion
    }
}
