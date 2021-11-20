package com.ilerna.pmdm.pacdedesarrollo.otrasClasesDeAyuda

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.ilerna.pmdm.pacdedesarrollo.R
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.Usuario
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.UsuarioApp
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.UsuarioDao
import com.ilerna.pmdm.pacdedesarrollo.databinding.ActivityAgregarDatosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Activity adicional para agregar datos a la base de datos.
 * Solicita los datos a agregar (Nombre y Telefono), pero no el ID porque este se
 * generara automaticamente.
 * Una vez agregado el dato, se ,muestra un dialogo informando sobre el ID generado.
 * Si se desea abortar la operacion se dispone de un boton CANCELAR para volver hacia atras
 */
class ActivityAgregarDatos : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarDatosBinding
    private lateinit var usuarioDao: UsuarioDao
    private var telOk = false
    private var nomOk = false
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAgregarDatosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioDao = UsuarioApp.getDatabase().usuarioDao()

        //Título de la activity
        this.supportActionBar?.title = getString(R.string.titulo_act_gregar)

        //Boton para incertar los datos a la base da datos
        binding.btnAdd.setOnClickListener {
            val context = this // guardar el contexto
            val nombre = binding.etNombre.text.toString().trim()
            val telefono = binding.etTelefono.text.toString().trim()
            //Verificar que hay telefono introducido
            if (telefono.isEmpty()) {
                binding.tilTelefono.error = getString(R.string.requerido)
                binding.etTelefono.requestFocus()
            } else {
                telOk = true
            }
            //Verificar que hay nombre introducido
            if (nombre.isEmpty()) {
                binding.tilNombre.error = getString(R.string.requerido)
                binding.etNombre.requestFocus()
            } else {
                nomOk = true
            }
            //Si los datos estan bien tecleados procedemos a insertar en la base de datos
            if (nomOk && telOk) {
                hideSoftKeyboard()
                //Agregar el dato con una corrutina
                CoroutineScope(Dispatchers.IO).launch {
                    val usuarioDao = UsuarioApp.getDatabase().usuarioDao()
                    val usuario = Usuario(nombre = nombre, telefono = telefono)
                    id = usuarioDao.addUsuario(usuario) //Obtener el ID al introducir el dato
                    if (id != null) {
                        //Se ha insertado y ha devuelto ID -> Informamos con un dialogo
                        runOnUiThread {
                            val dialogo = AlertDialog.Builder(context)
                            val aux = getString(R.string.id_generado) + "\n\n$id"
                            dialogo.setMessage(aux)
                                .setTitle(getString(R.string.usuario_agregado))
                                .setPositiveButton(R.string.aceptar) { _, _ -> finish() }
                                .create().show()
                        }
                    } else {
                        //No ha sido posible insertar -> informamos con un dialogo
                        runOnUiThread {
                            val dialogo = AlertDialog.Builder(context)
                            dialogo.setMessage(getString(R.string.imposible_agregar))
                                .setTitle(getString(R.string.error))
                                .create().show()
                        }
                    }
                }
            }
        }//binding.btnAdd.setOnClickListener

        //Si se presiona el boton de cancelar, se cancela la accion y se vuelve atras
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        //Cuando se presiona el enter se esconede el teclado y se quita el foco
        binding.etNombre.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftKeyboard()
                binding.etNombre.clearFocus()
                binding.etNombre.isCursorVisible = false

            }
            false
        }//binding.etNombre.setOnKeyListener

        //Cuando se presiona el enter se esconede el teclado y se quita el foco
        binding.etTelefono.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftKeyboard()
                binding.etTelefono.clearFocus()
                binding.etTelefono.isCursorVisible = false

            }
            false
        }//binding.etTelefono.setOnKeyListener

        //Si se escribe algo en el campo Nombre, este listener hace que deje de dar error
        binding.etNombre.addTextChangedListener {
            binding.tilNombre.error = null
        }
        //Si se escribe algo en el campo Telefono, este listener hace que deje de dar error
        binding.etTelefono.addTextChangedListener {
            binding.tilTelefono.error = null
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

}//ActivityAgregarDatos