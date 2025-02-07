package com.example.clientereparacion.modelo

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Reparacion (
    var id: Int,
    var descripcion: String,
    var fechaReparacion: Date,
    var costo: Double,
    var garantia: Boolean,
    var cliente_id: Int,
    var taller: Taller
):Parcelable{
    constructor(parcel: Parcel): this(
        parcel.readInt(),
        parcel.readString()!!,
        Date(parcel.readLong()),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readParcelable(Taller::class.java.classLoader)!!
    )

    constructor(descripcion: String, fechaReparacion: Date, costo: Double, garantia: Boolean, cliente_id: Int, taller: Taller) : this(
        0, // id por defecto
        descripcion,
        fechaReparacion,
        costo,
        garantia,
        cliente_id,
        taller
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(descripcion)
        parcel.writeLong(fechaReparacion.time)
        parcel.writeDouble(costo)
        parcel.writeByte(if (garantia) 1 else 0)
        parcel.writeInt(cliente_id)
        parcel.writeParcelable(taller, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Reparacion>{
        override fun createFromParcel(parcel: Parcel): Reparacion {
            return Reparacion(parcel)
        }

        override fun newArray(size: Int): Array<Reparacion?> {
            return arrayOfNulls(size)
        }
    }
}
