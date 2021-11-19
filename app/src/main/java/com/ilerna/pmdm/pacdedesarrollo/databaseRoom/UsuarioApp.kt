package com.ilerna.pmdm.pacdedesarrollo.databaseRoom

import android.app.Application

/**
 * Clase para inicializar la base de datos.
 */
class UsuarioApp : Application() {

    companion object {
        private var db: UsuarioDatabase? = null
        fun getDatabase(): UsuarioDatabase = db!!
    }

    override fun onCreate() {
        super.onCreate()
        db = UsuarioDatabase.getDatabase(applicationContext)

    }//onCreate

}//UsuarioApp