package com.ilerna.pmdm.pacdedesarrollo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase (data class) que representa nuestra tabla Usuario
 */
@Entity(tableName = "Usuario")
data class Usuario(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, //Clave Primaria de la tabla
    val nombre: String,
    val telefono: String

)//Usuario
