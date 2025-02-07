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
    2 // Incrementamos la versión de la base de datos
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
            taller_id INTEGER,
            taller_nombre VARCHAR(50),
            taller_latitud DOUBLE,
            taller_longitud DOUBLE,
            FOREIGN KEY(cliente_id) REFERENCES CLIENTE(id) ON DELETE CASCADE
        );
    """
        db?.execSQL(crearTablaReparaciones)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Manejar la actualización de la base de datos
        if (oldVersion < 2) {
            // Agregar las nuevas columnas a la tabla REPARACION
            db?.execSQL("ALTER TABLE REPARACION ADD COLUMN taller_id INTEGER;")
            db?.execSQL("ALTER TABLE REPARACION ADD COLUMN taller_nombre VARCHAR(50);")
            db?.execSQL("ALTER TABLE REPARACION ADD COLUMN taller_latitud DOUBLE;")
            db?.execSQL("ALTER TABLE REPARACION ADD COLUMN taller_longitud DOUBLE;")
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        db?.setForeignKeyConstraintsEnabled(true)
    }
}
