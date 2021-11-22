package com.ilerna.pmdm.pacdedesarrollo.activitiesPrincipales

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.ilerna.pmdm.pacdedesarrollo.R
import com.ilerna.pmdm.pacdedesarrollo.databinding.Activity1Binding

//Nombre del usuario de la app para dar la bienvenida al ir de una activity a otra
private var name: String? = null

/**
 * Activity 1 que contiene obligatoriamente:
 *  - 1 imagen
 *  - 2 botones (play y stop)
 *  - 3 botones para ir a las Activity 2, 3 y 4
 *  - Además agrega mas funcionalidad: 1 botón pause, un campo para pedir el nombre, usar Toasts y mas
 */
class Activity1 : AppCompatActivity() {

    private lateinit var binding: Activity1Binding
    private var mp: MediaPlayer? = null
    private var position = 0

    //Usamos para lanzar las activities (Es 1 de varias posibilidades de hacerlo)
    //Aunque no esperamos resultado, se puede utilizar.
    private val resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    /*
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = Activity1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //Título de la activity
        this.supportActionBar?.title = getString(R.string.titulo_act1)

        //Si no es la primera vez que vamos a la Activity1, hemos guardado el nombre
        //para no tener que introducirlo cada vez.
        name?.let {
            binding.etName.setText(name)
        }

        mp = MediaPlayer.create(this, R.raw.track)

        //Acción para cuando termine la canción: Preparar para poder reproducir de nuevo
        mp?.setOnCompletionListener {
            mp!!.stop()
            position = 0
            mp!!.prepare()
        }

        //Implementar Listener del botón PLAY
        binding.btnPlay.setOnClickListener {
            if (mp?.isPlaying != true) {
                mp!!.seekTo(position)
                mp!!.start()
            }
            Toast.makeText(this, getString(R.string.boron_play_pulsado), Toast.LENGTH_SHORT).show()
        }//btnPlay.setOnClickListener

        //Implementar Listener del botón PAUSE
        binding.btnPause.setOnClickListener {
            if (mp?.isPlaying == true) {
                position = mp!!.currentPosition
                mp!!.pause()
            }
            Toast.makeText(this, getString(R.string.boton_pause_pulsado), Toast.LENGTH_SHORT).show()
        }//btnPause.setOnClickListener

        //Implementar Listener del botón STOP
        binding.btnStop.setOnClickListener {
            mp?.stop()
            position = 0
            mp?.prepare()
            Toast.makeText(this, getString(R.string.boton_stop_pulsado), Toast.LENGTH_SHORT).show()
        }//btnStop.setOnClickListener

        //Cuando se presiona el enter se esconde el teclado y se quita el foco
        binding.etName.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftKeyboard()
                binding.etName.clearFocus()
                binding.etName.isCursorVisible = false

            }
            false
        }//binding.etName.setOnKeyListener

        //Botón para ir a Activity 2
        binding.btnAct2.setOnClickListener {
            if (nombreValido()) {
                name = binding.etName.text.toString()
                //Lanzar la Activity 2
                val intent = Intent(this, Activity2::class.java)
                intent.putExtra("nombre", name)
                resultContract.launch(intent)
                //Finalizar la Activity 1
                finish()
                Toast.makeText(this, getString(R.string.lanzar_act2), Toast.LENGTH_SHORT).show()
            }
        }
        //Botón para ir a Activity 3
        binding.btnAct3.setOnClickListener {
            if (nombreValido()) {
                name = binding.etName.text.toString()
                //Lanzar la Activity 3
                val intent = Intent(this, Activity3::class.java)
                intent.putExtra("nombre", name)
                resultContract.launch(intent)
                //Finalizar la Activity 1
                finish()
                Toast.makeText(this, getString(R.string.lanzar_act3), Toast.LENGTH_SHORT).show()
            }
        }
        //Botón para ir a Activity 4
        binding.btnAct4.setOnClickListener {
            name = binding.etName.text.toString()
            if (nombreValido()) {
                //Lanzar la Activity 4
                val intent = Intent(this, Activity4::class.java)
                intent.putExtra("nombre", name)
                resultContract.launch(intent)
                //Finalizar la Activity 1
                finish()
                Toast.makeText(this, getString(R.string.lanzar_act4), Toast.LENGTH_SHORT).show()
            }
        }

        //Si se escribe algo en el campo Nombre, este listener hace que deje de dar error
        binding.etName.addTextChangedListener {
            binding.tilName.error = null
        }

    }//onCreate

    /*
     * onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
        //Al destruir la activity, liberamos los recursos del media player
        mp?.stop()
        mp?.release()
    }//onDestroy

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
    private fun nombreValido(): Boolean {
        return if (binding.etName.text.toString().trim().isEmpty()) {
            binding.tilName.error = getString(R.string.requerido)
            binding.etName.requestFocus()
            false
        } else true
    }//checkName

}//MainActivity
