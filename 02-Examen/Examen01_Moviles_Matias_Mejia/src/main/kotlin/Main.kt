import modelo.Cliente
import persistencia.XmlHandler
import controlador.*
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val xmlHandler = XmlHandler("taller.xml")
    val clientes = xmlHandler.cargarClientes()

    while (true) {

        println("TALLER DE REPARACIÓN DE BICICLETAS LA MACHINE")
        println("Menú Principal:")
        println("1. Gestionar Clientes")
        println("2. Gestionar Reparaciones")
        println("3. Salir")
        when (scanner.nextInt()) {
            1 -> gestionarClientes(clientes, xmlHandler, scanner)
            2 -> gestionarReparaciones(clientes, xmlHandler, scanner)
            3 -> {
                println("Saliendo del sistema...")
                return
            }
            else -> println("Opción no válida, intente nuevamente.")
        }
    }
}

fun gestionarClientes(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    while (true) {
        println("TALLER DE REPARACIÓN DE BICICLETAS LA MACHINE")
        println("Gestión de Clientes:")
        println("1. Crear Cliente")
        println("2. Leer Clientes")
        println("3. Actualizar Cliente")
        println("4. Eliminar Cliente")
        println("5. Regresar al Menú Principal")
        when (scanner.nextInt()) {
            1 -> crearCliente(clientes, xmlHandler, scanner)
            2 -> leerClientes(clientes)
            3 -> actualizarCliente(clientes, xmlHandler, scanner)
            4 -> eliminarCliente(clientes, xmlHandler, scanner)
            5 -> return
            else -> println("Opción no válida.")
        }
    }
}

fun gestionarReparaciones(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    while (true) {
        println("TALLER DE REPARACIÓN DE BICICLETAS LA MACHINE")
        println("Gestión de Reparaciones:")
        println("1. Agregar Reparación")
        println("2. Leer Reparaciones")
        println("3. Actualizar Reparación")
        println("4. Eliminar Reparación")
        println("5. Regresar al Menú Principal")
        when (scanner.nextInt()) {
            1 -> agregarReparacion(clientes, xmlHandler, scanner)
            2 -> leerReparaciones(clientes, scanner)
            3 -> actualizarReparacion(clientes, xmlHandler, scanner)
            4 -> eliminarReparacion(clientes, xmlHandler, scanner)
            5 -> return
            else -> println("Opción no válida.")
        }
    }
}