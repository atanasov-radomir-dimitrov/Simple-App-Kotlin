package com.ilerna.pmdm.pacdedesarrollo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * Interfaz DAO para acceder a la base de datos cuando sea necesario.
 * Solamente estan implementadas las funciones que hacen falta para el desarrollo de la PAC.
 */
@Dao
interface UsuarioDao {

    @Query("SELECT * FROM Usuario WHERE id = :id")
    suspend fun getUsuarioById(id: Long): Usuario

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUsuario(usuario: Usuario): Long

}//UsuarioDao