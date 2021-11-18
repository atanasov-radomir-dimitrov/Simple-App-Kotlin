package com.ilerna.pmdm.pacdedesarrollo

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.ilerna.pmdm.pacdedesarrollo.databinding.Activity2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity 2 que contiene obligatoriamente:
 *  - Implementacion de una base de datos (en mi caso con Room)
 *  - Opcion para consultar la base de datos
 *  - Opcion para agregar dato a la base de datos
 *  - Boton para volver a la Activity 1
 *  - Ademas, se agregan comprobaciones de los campos a introducir, utilizacion de FAB, una activity
 *    nueva para agregar nuevo usuario a la base de datos, etc...
 */
class Activity2 : AppCompatActivity(), Aux {

    private lateinit var binding: Activity2Binding
    private val resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            binding.fab.show()
        }

    /*
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Activity2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //Título
        this.supportActionBar?.title = "Activity2"

        //Obtenemos el nombre enviado desde la Activity 1 y mostramos bienvenida por dialogo
        intent.extras?.getString("nombre")?.let { dialogobienvenida(this, it) }

        //Lanzar la Activity 1
        binding.btnAct1.setOnClickListener {
            //Lanzar la Activity 1
            val intent = Intent(this, Activity1::class.java)
            resultContract.launch(intent)
            //Finalizamos esta activity
            finish()
        }//binding.btnAct1.setOnClickListener

        //Cuando se presiona el enter se esconede el teclado y se quita el foco
        binding.etBusqueda.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftKeyboard()
                binding.etBusqueda.clearFocus()
                binding.etBusqueda.isCursorVisible = false
            }
            false
        }//binding.etBusqueda.setOnKeyListener

        //Si se escribe algo en el campo ID, este listener hace que deje de dar error
        binding.etBusqueda.addTextChangedListener {
            binding.tilBusqueda.error = null
        }

        //Buscar un dato
        binding.btnBuscar.setOnClickListener {
            val id = binding.etBusqueda.text.toString().trim()
            val context = this // guardar el contexto
            if (checkInputValido()) {
                hideSoftKeyboard()
                //Corrutina para ejecutar la búsqueda de datos en la base de datos
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val usuarioDao = UsuarioApp.getDatabase().usuarioDao()
                        val usuarioPrueba: Usuario? = usuarioDao.getUsuarioById(id.toLong())
                        if (usuarioPrueba != null) {
                            //Dato existe, lo mostramos con un dialog
                            Handler(Looper.getMainLooper()).post {
                                val dialogo = AlertDialog.Builder(context)
                                dialogo.setMessage(
                                    "ID: ${usuarioPrueba.id}\n" +
                                            "Nombre: ${usuarioPrueba.nombre}\n" +
                                            "Teléfono: ${usuarioPrueba.telefono}"
                                )
                                    .setPositiveButton(R.string.aceptar) { _, _ ->
                                        //No hacemos nada, solo aceptar para cerrar el dialogo
                                    }
                                    .setTitle(getString(R.string.datos_usuario))
                                    .create().show()
                                //Anulamos el campo del edit text
                                binding.etBusqueda.setText("")
                            }
                        } else {
                            //DATO no encontrado: Informamos con un dialogo
                            Handler(Looper.getMainLooper()).post {
                                val dialogo = AlertDialog.Builder(context)
                                dialogo.setMessage(R.string.dato_no_existe)
                                    .setPositiveButton(R.string.aceptar) { _, _ ->
                                        //No hacemos nada, solo aceptar para cerrar el dialogo
                                    }.create().show()
                                //Anulamos el campo del edit text
                                binding.etBusqueda.setText("")
                            }
                        }
                    }
                }
            }
        }//binding.btnBuscar.setOnClickListener

        //Listener del Floating Action Button (FAB, para abrir nueva Activity para agregar usuario)
        binding.fab.setOnClickListener {
            val intent = Intent(this, ActivityAgregarDatos::class.java)
            resultContract.launch(intent)
            binding.fab.hide()
        }

    }//onCreate


    /**
     * Esconder el teclado
     */
    private fun hideSoftKeyboard() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }//hideSoftKeyboard

    /**
     * Comprobar que el nombre que contiene el edit text del nombre contenga datos
     */
    private fun checkInputValido(): Boolean {
        return if (binding.etBusqueda.text.toString().trim().isEmpty()) {
            binding.tilBusqueda.error = getString(R.string.requerido)
            binding.etBusqueda.requestFocus()
            false
        } else true
    }//checkName

}//Activity2
