package com.ilerna.pmdm.pacdedesarrollo

import android.app.AlertDialog
import android.content.Context

/**
 * Interfaz con funcionalidades auxiliares
 */
interface Aux {

    /**
     * Funcion que crea y muestra un dialogo para dar la bienvenida a la Activity
     *
     * @param context El contexto donde va a aparecer el dialogo
     * @param nombre El nombre que se agregara al dialogo para la bienvenida
     */
    fun dialogobienvenida(context: Context, nombre: String) {
        //Dialogo para informar en qué Activity estamos
        val dialogo = AlertDialog.Builder(context)
        dialogo.setMessage("Bienvenida/o a la Activity 2, $nombre")
            .setPositiveButton(R.string.aceptar) { _, _ ->
                //No hacemos nada, solo aceptar para cerrar el dialogo
            }.create().show()
    }//dialogobienvenida

}//Aux