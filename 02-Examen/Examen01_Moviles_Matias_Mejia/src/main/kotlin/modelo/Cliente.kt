package modelo

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.Date

@Root(name = "cliente")
data class Cliente(
    @field:Element(name = "id")
    var id: Int = 0,
    @field:Element(name = "nombre")
    var nombre: String = "",
    @field:Element(name = "fechaRegistro")
    var fechaRegistro: Date = Date(),
    @field:Element(name = "esMiembro")
    var esMiembro: Boolean = false,
    @field:ElementList(name = "reparaciones", inline = true, required = false)
    var reparaciones: MutableList<Reparacion> = mutableListOf()
)