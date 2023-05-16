package com.example.administradordeunidadesmviles_e2023

import android.util.Log

class ValoresGlobales{
    private var NombreUser: String=""

    fun setNombreUser(nombreRecibido:String) {
        NombreUser=nombreRecibido
    }

    fun getNombreUser():String{
        return NombreUser
    }




}