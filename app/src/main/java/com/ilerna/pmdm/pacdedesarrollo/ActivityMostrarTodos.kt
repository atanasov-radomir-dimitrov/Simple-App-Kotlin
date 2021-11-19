package com.ilerna.pmdm.pacdedesarrollo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.Usuario
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.UsuarioApp
import com.ilerna.pmdm.pacdedesarrollo.databinding.ActivityMostrarTodosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity que muestra todos los datos de la base de datos utilizando RecyclerView
 */
class ActivityMostrarTodos : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarTodosBinding

    //Lista con los datos
    private lateinit var listaUsuarios: List<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMostrarTodosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Título de la activity
        this.supportActionBar?.title = getString(R.string.titulo_act_mostrar_todos)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                //Obtenemos la lsita con los datos que despues vamos a visualizar
                listaUsuarios = UsuarioApp.getDatabase().usuarioDao().getAllUsuarios()
                //Iniciamos el recyclerview
                iniciarRecycler()
            }
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