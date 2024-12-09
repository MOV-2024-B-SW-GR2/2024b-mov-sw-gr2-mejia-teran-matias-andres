package modelo

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.util.*

@Root(name = "reparacion")
data class Reparacion(
    @field:Element(name = "id")
    var id: Int = 0,
    @field:Element(name = "descripcion")
    var descripcion: String = "",
    @field:Element(name = "fechaReparacion")
    var fechaReparacion: Date = Date(),
    @field:Element(name = "costo")
    var costo: Double = 0.0,
    @field:Element(name = "duracionHoras")
    var duracionHoras: Int = 0,
    @field:Element(name = "garantia")
    var garantia: Boolean = false
)