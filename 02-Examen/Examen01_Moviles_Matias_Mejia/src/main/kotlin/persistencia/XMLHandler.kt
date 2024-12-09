package persistencia

import modelo.Cliente
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.File

@Root(name = "taller")
class TallerXml(
    @field:ElementList(name = "clientes", inline = true)
    var clientes: MutableList<Cliente> = mutableListOf()
)

class XmlHandler(private val filePath: String) {

    private val serializer = org.simpleframework.xml.core.Persister()

    fun guardarClientes(clientes: List<Cliente>) {
        val taller = TallerXml(clientes.toMutableList())
        val file = File(filePath)
        serializer.write(taller, file)
    }

    fun cargarClientes(): MutableList<Cliente> {
        val file = File(filePath)
        if (!file.exists()) return mutableListOf()
        return serializer.read(TallerXml::class.java, file).clientes
    }
}
