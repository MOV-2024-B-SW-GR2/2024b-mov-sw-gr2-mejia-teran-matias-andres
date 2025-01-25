package com.example.clientereparacion

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaClientes = """
        CREATE TABLE CLIENTE(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(50),
            telefono VARCHAR(20),
            fecha_registro VARCHAR(50),
            es_miembro BOOLEAN
        );
    """
        db?.execSQL(crearTablaClientes)

        val crearTablaReparaciones = """
        CREATE TABLE REPARACION(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            descripcion VARCHAR(50),
            fecha_reparacion VARCHAR(50),
            costo DOUBLE,
            garantia BOOLEAN,
            cliente_id INTEGER,
            FOREIGN KEY(cliente_id) REFERENCES CLIENTE(id) ON DELETE CASCADE
        );
    """
        db?.execSQL(crearTablaReparaciones)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar las actualizaciones de la base de datos si cambias la estructura
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        // Aquí puedes configurar la base de datos si es necesario
    }
}
