package com.example.clientereparacion.modelo

import android.os.Parcel
import android.os.Parcelable

data class Taller(
    val id: Int,
    val nombre: String,
    val latitud: Double,
    val longitud: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(latitud)
        parcel.writeDouble(longitud)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Taller> {
        override fun createFromParcel(parcel: Parcel): Taller {
            return Taller(parcel)
        }

        override fun newArray(size: Int): Array<Taller?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return nombre
    }
}
