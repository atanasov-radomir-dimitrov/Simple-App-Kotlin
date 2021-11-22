package com.ilerna.pmdm.pacdedesarrollo.databaseRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * Interfaz DAO para acceder a la base de datos cuando sea necesario.
 * Solamente están implementadas las funciones que hacen falta para el desarrollo de la PAC.
 */
@Dao
interface UsuarioDao {

    @Query("SELECT * FROM Usuario WHERE id = :id")
    suspend fun getUsuarioById(id: Long): Usuario

    @Query("SELECT * FROM Usuario")
    suspend fun getAllUsuarios(): List<Usuario>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUsuario(usuario: Usuario): Long

    @Query("SELECT COUNT(id) FROM Usuario")
    suspend fun getSize(): Long

}//UsuarioDao