package com.ilerna.pmdm.pacdedesarrollo.otrasClasesDeAyuda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilerna.pmdm.pacdedesarrollo.R
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.Usuario
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.UsuarioApp
import com.ilerna.pmdm.pacdedesarrollo.databinding.ActivityMostrarTodosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Activity que muestra todos los datos de la base de datos utilizando RecyclerView
 */
class ActivityMostrarTodos : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarTodosBinding

    //Lista con los datos
    private var listaUsuarios: List<Usuario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMostrarTodosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Título de la activity
        this.supportActionBar?.title = getString(R.string.titulo_act_mostrar_todos)

        //Corrutina: Obtenemos la lista con los datos que después vamos a visualizar
        CoroutineScope(Dispatchers.IO).launch {
            listaUsuarios = UsuarioApp.getDatabase().usuarioDao().getAllUsuarios()
            //Iniciamos el recyclerview
            iniciarRecycler()
        }

        //Salir de la Activity cuando se presiones el botón salir
        binding.btnSalir.setOnClickListener {
            finish()
        }

    }//onCreate

    /**
     * Iniciar el adaptador para mostrar los datos
     */
    private fun iniciarRecycler() {
        binding.rvUsuario.layoutManager = LinearLayoutManager(this)
        val adapter = UsuarioAdapter(listaUsuarios)
        binding.rvUsuario.adapter = adapter
    }//iniciarRecycler

}//ActivityMostrarTodos