package controlador

import modelo.Cliente
import persistencia.XmlHandler
import java.text.SimpleDateFormat
import java.util.*

fun crearCliente(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    println("Ingrese ID del cliente (único):")
    val id = scanner.nextInt()
    if (clientes.any { it.id == id }) {
        println("Error: Ya existe un cliente con ese ID.")
        return
    }

    scanner.nextLine() // Consumir salto de línea
    println("Ingrese nombre del cliente (solo letras y espacios):")
    val nombre = scanner.nextLine()
    if (!nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
        println("Error: El nombre contiene caracteres no permitidos.")
        return
    }

    println("Ingrese fecha de registro (yyyy-MM-dd):")
    val fechaInput = scanner.nextLine()
    val fechaRegistro = try {
        validarYConvertirFecha(fechaInput)
    } catch (e: Exception) {
        println("Error: ${e.message}")
        return
    }

    println("¿Es miembro del club? (true/false):")
    val esMiembro = try {
        scanner.nextBoolean()
    } catch (e: Exception) {
        println("Error: Valor inválido para la membresía (debe ser true o false).")
        scanner.nextLine()
        return
    }

    val nuevoCliente = Cliente(id, nombre, fechaRegistro, esMiembro)
    clientes.add(nuevoCliente)
    xmlHandler.guardarClientes(clientes)
    println("Cliente creado exitosamente.")
}



fun leerClientes(clientes: MutableList<Cliente>) {
    if (clientes.isEmpty()) {
        println("No hay clientes registrados.")
    } else {
        println("=== Lista de Clientes ===")
        for (cliente in clientes) {
            println("ID: ${cliente.id}")
            println("Nombre: ${cliente.nombre}")
            println("Fecha de Registro: ${cliente.fechaRegistro}")
            println("¿Es miembro del club?: ${if (cliente.esMiembro) "Sí" else "No"}")
            println("Reparaciones Asociadas: ${cliente.reparaciones.size}")
            println("-------------------------")
        }
    }
}

fun actualizarCliente(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    println("Ingrese el ID del cliente a actualizar:")
    val id = scanner.nextInt()
    val cliente = clientes.find { it.id == id }

    if (cliente == null) {
        println("Error: No existe un cliente con ese ID.")
        return
    }

    scanner.nextLine() // Consumir salto de línea
    println("Ingrese el nuevo nombre del cliente (dejar vacío para no cambiar):")
    val nombre = scanner.nextLine()
    if (nombre.isNotEmpty() && !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
        println("Error: El nombre contiene caracteres no permitidos.")
        return
    }
    if (nombre.isNotEmpty()) cliente.nombre = nombre

    println("Ingrese la nueva fecha de registro (yyyy-MM-dd, dejar vacío para no cambiar):")
    val fechaInput = scanner.nextLine()
    if (fechaInput.isNotEmpty()) {
        try {
            cliente.fechaRegistro = validarYConvertirFecha(fechaInput)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return
        }
    }

    println("¿Es miembro del club? (true/false, dejar vacío para no cambiar):")
    val esMiembroInput = scanner.nextLine()
    if (esMiembroInput.isNotEmpty()) {
        cliente.esMiembro = esMiembroInput.toBooleanStrictOrNull() ?: run {
            println("Error: Valor inválido para la membresía (debe ser true o false).")
            return
        }
    }

    xmlHandler.guardarClientes(clientes)
    println("Cliente actualizado exitosamente.")
}


fun eliminarCliente(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    println("Ingrese el ID del cliente a eliminar:")
    val id = scanner.nextInt()
    if (clientes.removeIf { it.id == id }) {
        xmlHandler.guardarClientes(clientes)
        println("Cliente eliminado exitosamente.")
    } else {
        println("Cliente no encontrado.")
    }
}

fun validarYConvertirFecha(fecha: String): Date {
    val formato = SimpleDateFormat("yyyy-MM-dd")
    formato.isLenient = false // Evita interpretaciones automáticas de fechas no válidas
    return try {
        val fechaConvertida = formato.parse(fecha)
        if (fechaConvertida.after(Date())) {
            throw IllegalArgumentException("La fecha no puede ser superior a la actual.")
        }
        fechaConvertida
    } catch (e: Exception) {
        throw IllegalArgumentException("El formato de la fecha debe ser yyyy-MM-dd.")
    }
}
