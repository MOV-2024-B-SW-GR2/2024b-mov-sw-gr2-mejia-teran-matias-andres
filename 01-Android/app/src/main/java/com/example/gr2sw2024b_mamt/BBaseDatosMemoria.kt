package com.example.gr2sw2024b_mamt

class BBaseDatosMemoria {
    companion object{
        var arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1, "Matias", "mtmatias12@gmail.com"))
            arregloBEntrenador.add(BEntrenador(2, "Carlos", "cbayas@gmail.com"))
            arregloBEntrenador.add(BEntrenador(3, "Santiago", "sbejarano@gmail.com"))
        }
    }

}