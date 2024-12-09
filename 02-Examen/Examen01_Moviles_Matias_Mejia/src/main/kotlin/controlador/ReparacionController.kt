package controlador

import modelo.Cliente
import modelo.Reparacion
import persistencia.XmlHandler
import java.util.Scanner

fun agregarReparacion(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    println("Ingrese el ID del cliente para agregar la reparación:")
    val idCliente = scanner.nextInt()
    val cliente = clientes.find { it.id == idCliente }

    if (cliente == null) {
        println("Error: No se encontró un cliente con el ID especificado.")
        return
    }

    println("Ingrese el ID único de la reparación:")
    val idReparacion = scanner.nextInt()
    if (cliente.reparaciones.any { it.id == idReparacion }) {
        println("Error: Ya existe una reparación con ese ID para este cliente.")
        return
    }

    scanner.nextLine() // Consumir salto de línea
    println("Ingrese la descripción del problema (solo letras y espacios):")
    val descripcion = scanner.nextLine()
    if (!descripcion.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+$"))) {
        println("Error: La descripción contiene caracteres no permitidos.")
        return
    }

    println("Ingrese la fecha de reparación (yyyy-MM-dd):")
    val fechaInput = scanner.nextLine()
    val fechaReparacion = try {
        validarYConvertirFecha(fechaInput)
    } catch (e: Exception) {
        println("Error: ${e.message}")
        return
    }

    println("Ingrese el costo de la reparación (número positivo):")
    val costo = try {
        val valor = scanner.nextDouble()
        if (valor < 0) throw IllegalArgumentException("El costo debe ser un valor positivo.")
        valor
    } catch (e: Exception) {
        println("Error: ${e.message}")
        scanner.nextLine()
        return
    }

    println("Ingrese la duración de la reparación en horas (número entero positivo):")
    val duracion = try {
        val valor = scanner.nextInt()
        if (valor <= 0) throw IllegalArgumentException("La duración debe ser un número entero positivo.")
        valor
    } catch (e: Exception) {
        println("Error: ${e.message}")
        scanner.nextLine()
        return
    }

    println("¿La reparación incluye garantía? (true/false):")
    val garantia = try {
        scanner.nextBoolean()
    } catch (e: Exception) {
        println("Error: Valor inválido para la garantía (debe ser true o false).")
        scanner.nextLine()
        return
    }

    val nuevaReparacion = Reparacion(idReparacion, descripcion, fechaReparacion, costo, duracion, garantia)
    cliente.reparaciones.add(nuevaReparacion)
    xmlHandler.guardarClientes(clientes)
    println("Reparación creada exitosamente para el cliente con ID $idCliente.")
}

fun leerReparaciones(clientes: MutableList<Cliente>, scanner: Scanner) {
    if (clientes.isEmpty()) {
        println("No hay clientes registrados para mostrar reparaciones.")
        return
    }

    println("Ingrese el ID del cliente para ver sus reparaciones:")
    val clienteId = scanner.nextInt()
    val cliente = clientes.find { it.id == clienteId }

    if (cliente == null) {
        println("Cliente no encontrado.")
        return
    }

    if (cliente.reparaciones.isEmpty()) {
        println("El cliente ${cliente.nombre} no tiene reparaciones registradas.")
    } else {
        println("=== Reparaciones para el Cliente: ${cliente.nombre} ===")
        for ((index, reparacion) in cliente.reparaciones.withIndex()) {
            println("Reparación #${index + 1}")
            println("ID: ${reparacion.id}")
            println("Descripción: ${reparacion.descripcion}")
            println("Fecha de Reparación: ${reparacion.fechaReparacion}")
            println("Costo: $${reparacion.costo}")
            println("Duración en Horas: ${reparacion.duracionHoras}")
            println("Garantía: ${if (reparacion.garantia) "Sí" else "No"}")
            println("-----------------------------")
        }
    }
}

fun actualizarReparacion(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    println("Ingrese el ID del cliente que tiene la reparación a actualizar:")
    val idCliente = scanner.nextInt()
    val cliente = clientes.find { it.id == idCliente }

    if (cliente == null) {
        println("Error: No se encontró un cliente con el ID especificado.")
        return
    }

    println("Ingrese el ID de la reparación a actualizar:")
    val idReparacion = scanner.nextInt()
    val reparacion = cliente.reparaciones.find { it.id == idReparacion }

    if (reparacion == null) {
        println("Error: No se encontró una reparación con el ID especificado.")
        return
    }

    scanner.nextLine() // Consumir salto de línea
    println("Ingrese la nueva descripción del problema (dejar vacío para no cambiar):")
    val descripcion = scanner.nextLine()
    if (descripcion.isNotEmpty() && !descripcion.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+$"))) {
        println("Error: La descripción contiene caracteres no permitidos.")
        return
    }
    if (descripcion.isNotEmpty()) reparacion.descripcion = descripcion

    println("Ingrese la nueva fecha de reparación (yyyy-MM-dd, dejar vacío para no cambiar):")
    val fechaInput = scanner.nextLine()
    if (fechaInput.isNotEmpty()) {
        try {
            reparacion.fechaReparacion = validarYConvertirFecha(fechaInput)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return
        }
    }

    println("Ingrese el nuevo costo de la reparación (dejar vacío para no cambiar):")
    val costoInput = scanner.nextLine()
    if (costoInput.isNotEmpty()) {
        try {
            val costo = costoInput.toDouble()
            if (costo < 0) throw IllegalArgumentException("El costo debe ser un valor positivo.")
            reparacion.costo = costo
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return
        }
    }

    println("Ingrese la nueva duración en horas (dejar vacío para no cambiar):")
    val duracionInput = scanner.nextLine()
    if (duracionInput.isNotEmpty()) {
        try {
            val duracion = duracionInput.toInt()
            if (duracion <= 0) throw IllegalArgumentException("La duración debe ser un número entero positivo.")
            reparacion.duracionHoras = duracion
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return
        }
    }

    println("¿La reparación incluye garantía? (true/false, dejar vacío para no cambiar):")
    val garantiaInput = scanner.nextLine()
    if (garantiaInput.isNotEmpty()) {
        reparacion.garantia = garantiaInput.toBooleanStrictOrNull() ?: run {
            println("Error: Valor inválido para la garantía (debe ser true o false).")
            return
        }
    }

    xmlHandler.guardarClientes(clientes)
    println("Reparación actualizada exitosamente.")
}


fun eliminarReparacion(clientes: MutableList<Cliente>, xmlHandler: XmlHandler, scanner: Scanner) {
    println("Ingrese el ID del cliente:")
    val clienteId = scanner.nextInt()
    val cliente = clientes.find { it.id == clienteId }

    if (cliente != null) {
        println("Ingrese el ID de la reparación a eliminar:")
        val reparacionId = scanner.nextInt()

        if (cliente.reparaciones.removeIf { it.id == reparacionId }) {
            xmlHandler.guardarClientes(clientes)
            println("Reparación eliminada exitosamente.")
        } else {
            println("Reparación no encontrada.")
        }
    } else {
        println("Cliente no encontrado.")
    }
}