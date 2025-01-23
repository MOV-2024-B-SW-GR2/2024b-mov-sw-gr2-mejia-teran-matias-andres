package com.example.gr2sw2024b_mamt
import android.os.Parcel
import android.os.Parcelable

class BEntrenador(
    var id: Int,
    var nombre: String,
    var descripcion: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(), //estamos leyendo deserializando
        if(parcel.readString()== null) "" else parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "$nombre ${descripcion}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id) //escribiendo serializando el contenido
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BEntrenador> {
        override fun createFromParcel(parcel: Parcel): BEntrenador {
            return BEntrenador(parcel)
        }

        override fun newArray(size: Int): Array<BEntrenador?> {
            return arrayOfNulls(size)
        }
    }
}