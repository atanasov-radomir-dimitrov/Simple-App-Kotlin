package com.ilerna.pmdm.pacdedesarrollo.otrasClasesDeAyuda

import android.app.AlertDialog
import android.content.Context
import com.ilerna.pmdm.pacdedesarrollo.R

/**
 * Interfaz con funcionalidades auxiliares
 */
interface InterfazAuxiliar {

    /**
     * Funcion que crea y muestra un dialogo para dar la bienvenida a la Activity
     *
     * @param context El contexto donde va a aparecer el dialogo
     * @param activity Activity a la ques e accede
     * @param nombre El nombre que se agregara al dialogo para la bienvenida
     */
    fun dialogobienvenida(context: Context, activity: Int, nombre: String) {
        //Dialogo para informar en qué Activity estamos
        val dialogo = AlertDialog.Builder(context)
        dialogo.setMessage("Bienvenida/o a la Activity $activity, $nombre")
            .setPositiveButton(R.string.aceptar) { _, _ ->
                //No hacemos nada, solo aceptar para cerrar el dialogo
            }.create().show()
    }//dialogobienvenida

}//Aux