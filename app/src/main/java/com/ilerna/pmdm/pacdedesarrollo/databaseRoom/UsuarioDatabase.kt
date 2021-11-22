package com.ilerna.pmdm.pacdedesarrollo.databaseRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase abstracta que hereda de RoomDatabase y sirve para la creación de la base de datos.
 */
@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class UsuarioDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private var db: UsuarioDatabase? = null
        fun getDatabase(context: Context): UsuarioDatabase? {
            if (db == null) {
                db = Room.databaseBuilder(
                    context,
                    UsuarioDatabase::class.java,
                    "UsuarioDatabase"
                ).build()
            }
            return db!!
        }//getDatabase

    }//companion object

}//UsuarioDatabase
