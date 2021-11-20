package com.ilerna.pmdm.pacdedesarrollo.otrasClasesDeAyuda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilerna.pmdm.pacdedesarrollo.R
import com.ilerna.pmdm.pacdedesarrollo.databaseRoom.Usuario
import com.ilerna.pmdm.pacdedesarrollo.databinding.ItemUsuarioBinding

/**
 * Adapter para implementar neustro RecyclerView para visualizar los usuario de la base de datos
 */
class UsuarioAdapter(val usuario: List<Usuario>) :
    RecyclerView.Adapter<UsuarioAdapter.UsuarioHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioHolder(layoutInflater.inflate(R.layout.item_usuario, parent, false))
    }

    override fun onBindViewHolder(holder: UsuarioHolder, position: Int) {
        holder.render(usuario[position])
    }

    override fun getItemCount(): Int = usuario.size

    inner class UsuarioHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemUsuarioBinding.bind(view)

        fun render(usuario: Usuario) {
            binding.tvId.setText("ID: ${usuario.id}")
            binding.tvNombre.setText("NOMBRE:\n${usuario.nombre}")
            binding.tvTel.setText("TELÉFONO:\n${usuario.telefono}")
        }//render
    }//UsuarioHolder
}//UsuarioAdapter