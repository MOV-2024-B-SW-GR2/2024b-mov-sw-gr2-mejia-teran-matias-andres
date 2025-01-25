package com.example.clientereparacion.modelo

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Cliente(
    var id: Int,
    var nombre: String,
    var fechaRegistro: Date,
    var telefono: String,
    var esMiembro: Boolean,

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        Date(parcel.readLong()),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
    )

    constructor(nombre: String, fechaRegistro: Date, telefono: String, esMiembro: Boolean) : this(
        0,
        nombre,
        fechaRegistro,
        telefono,
        esMiembro
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeLong(fechaRegistro.time)
        parcel.writeString(telefono)
        parcel.writeByte(if (esMiembro) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cliente> {
        override fun createFromParcel(parcel: Parcel): Cliente {
            return Cliente(parcel)
        }

        override fun newArray(size: Int): Array<Cliente?> {
            return arrayOfNulls(size)
        }
    }
}
