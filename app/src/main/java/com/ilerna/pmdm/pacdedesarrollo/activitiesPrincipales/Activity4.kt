package com.ilerna.pmdm.pacdedesarrollo.activitiesPrincipales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.ilerna.pmdm.pacdedesarrollo.otrasClasesDeAyuda.InterfazAuxiliar
import com.ilerna.pmdm.pacdedesarrollo.R
import com.ilerna.pmdm.pacdedesarrollo.databinding.Activity4Binding

private var primeraEntradaActivity4 = true

/**
 * Activity 4 que contiene 6 imagenes
 *  - En posicion normal de la pantalla se ven las 6 una debajo de otra
 *  - Al girar el telefono se ven 2 filas de 3 iamgenes en cada fila
 *  - Tiene boton para vovler a la Activity 1
 */
class Activity4 : AppCompatActivity(), InterfazAuxiliar {

    private lateinit var binding: Activity4Binding
    private val resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    /*
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = Activity4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //Título de la activity
        this.supportActionBar?.title = getString(R.string.titulo_act4)

        //Esto se hace para que no se muestre el dialogo cada vez que se gira la pantalla
        if (primeraEntradaActivity4) {
            //Obtenemos el nombre enviado desde la Activity 1 y mostramos bienvenida por dialogo
            intent.extras?.getString("nombre")?.let { dialogobienvenida(this, 4, it) }
            primeraEntradaActivity4 = false
        }

        //Vovler a la Activity1
        binding.btnAct1.setOnClickListener {
            //Lanzar la Activity 1
            val intent = Intent(this, Activity1::class.java)
            resultContract.launch(intent)
            //Finalizamos esta activity
            finish()
        }

    }//onCreate

}//Activity4