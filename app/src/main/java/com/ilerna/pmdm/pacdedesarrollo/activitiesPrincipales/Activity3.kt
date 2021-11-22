package com.ilerna.pmdm.pacdedesarrollo.activitiesPrincipales

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ilerna.pmdm.pacdedesarrollo.otrasClasesDeAyuda.InterfazAuxiliar
import com.ilerna.pmdm.pacdedesarrollo.R
import com.ilerna.pmdm.pacdedesarrollo.databinding.Activity3Binding
import java.io.File

private lateinit var photoFile: File
private const val FILE_NAME = "photo.jpg"
private const val CAMARA_RQ = 17

/**
 * Activity 3 que contiene:
 *  - Botón para volver a la Activity1
 *  - Botón para lanzar la cámara.
 *  - Pide permisos para usar la cámara
 *  - Guarda la imagen en un archivo
 *  - Muestra la imagen tomada en un imageView
 */
class Activity3 : AppCompatActivity(), InterfazAuxiliar {

    private lateinit var binding: Activity3Binding
    private val resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //Resultado al volver de la Activity en la que se toma la imagen
            if (result.resultCode == Activity.RESULT_OK) {
                //Recibir los datos de la imagen
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                binding.textView.text = ""
                //Mostrar la imagen por pantalla
                binding.ivFoto.setImageBitmap(takenImage)
                //Informar con un dialogo de la ruta donde se guarda la imagen
                val dialogo = AlertDialog.Builder(this)
                dialogo.setMessage(photoFile.absolutePath)
                    .setTitle(getString(R.string.ruta_archivo))
                    .setPositiveButton(R.string.aceptar) { _, _ -> }
                    .create().show()
            }
        }

    /*
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = Activity3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //Título de la activity
        this.supportActionBar?.title = getString(R.string.titulo_act3)

        //Obtenemos el nombre enviado desde la Activity 1 y mostramos bienvenida por dialogo
        intent.extras?.getString("nombre")?.let { dialogobienvenida(this, 3, it) }

        //Volver a la Activity1
        binding.btnAct1.setOnClickListener {
            //Lanzar la Activity 1
            val intent = Intent(this, Activity1::class.java)
            resultContract.launch(intent)
            //Finalizamos esta activity
            finish()
        }

        //Acciones al pulsar el botón de la camara
        binding.btnCamera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Si estamos en una versión de Android superior a la 6 tenemos que comprobar los permisos
                checkPermissions()
            } else {
                //Sino, los permisos se conceden automáticamente y podemos abrir la cámara sin comprobarlos
                abrirCamara()
            }
        }

    }//onCreate

    /**
     * Comprobar si tiene permiso para usar la cámara. SI los tiene la abrimos, sino los pedimos
     */
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) { //Permiso no aceptado todavía
            requestCameraPermission()
        } else {
            //Abrir cámara
            abrirCamara()
        }
    }//checkPermissions

    /**
     * Pedir permisos para usar la cámara. Si se han rechazado anteriormente informamos de ello.
     * En caso contrario los solicitamos.
     */
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //El usuario ya ha rechazado anteriormente los permisos
            Toast.makeText(this, "Permiso rechazado", Toast.LENGTH_SHORT).show()
        } else {
            //Todavía no ha rechazado ni aceptado -> PEDIR
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMARA_RQ)
        }
    }//requestCameraPermission

    /*
     * Resultado de pedir el permiso
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMARA_RQ) { //Se trata de nuestro permiso
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permiso aceptado! POR FIN! :) -> Abrir Cámara
                abrirCamara()
            } else {
                //Permiso no fue aceptado
                Toast.makeText(this, "Permiso rechazado por primera vez", Toast.LENGTH_SHORT).show()
            }
        }
    }//onRequestPermissionsResult

    /**
     * Abrir la cámara para tomar la foto
     */
    private fun abrirCamara() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)
        // Donde guardar la foto
        val fileProvider = FileProvider.getUriForFile(
            this,
            "com.ilerna.pmdm.pacdedesarrollo.fileprovider",
            photoFile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        try {
            //Lanzamos la activity para usar la cámara
            resultContract.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            //No se puede abrir la cámara
            Toast.makeText(this, "Imposible abrir la cámara", Toast.LENGTH_SHORT).show()
        }
    }//abrirCamara

    /**
     * Obtener el fichero temporal donde se guardara la foto
     */
    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

}//Activity3