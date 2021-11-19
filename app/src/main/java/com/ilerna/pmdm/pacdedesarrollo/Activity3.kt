package com.ilerna.pmdm.pacdedesarrollo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.ilerna.pmdm.pacdedesarrollo.databinding.Activity3Binding
import java.io.File

private lateinit var photoFile: File
private const val FILE_NAME = "photo.jpg"
private const val CAMARA_RQ = 17


class Activity3 : AppCompatActivity(), Aux {

    private lateinit var binding: Activity3Binding
    private val resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Recibir los datos de la imagen
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                binding.textView.text = ""
                //Mostrar la imagen por pantalla
                binding.ivFoto.setImageBitmap(takenImage)
                //Informar con un dialogo de la ruta donde se guarda la iamgen
                val dialogo = AlertDialog.Builder(this)
                dialogo.setMessage(
                    "Ruta donde está guardada la imagen:\n\n" +
                            "${photoFile.absolutePath}"
                ).setTitle("Ruta archivo")
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

        //Título
        this.supportActionBar?.title = "Activity3"

        //Obtenemos el nombre enviado desde la Activity 1 y mostramos bienvenida por dialogo
        intent.extras?.getString("nombre")?.let { dialogobienvenida(this, it) }

        //Vovler a la Activity1
        binding.btnAct1.setOnClickListener {
            //Lanzar la Activity 1
            val intent = Intent(this, Activity1::class.java)
            resultContract.launch(intent)
            //Finalizamos esta activity
            finish()
        }

        //Acciones al pulsar el boton de la camara
        binding.btnCamera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Si estamos en una versión de Android superior a la 6 tenemos que comrpobar los permisops
                checkPermissions()
            } else {
                //Sino, estos se conceden automáticamente y podemos abrir la cámara sin mirar los permisos
                abrirCamara()
            }
        }

    }//onCreate

    /**
     * Comprobar si tiene permiso para usar la cámara
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
    }

    /**
     * Pedir permisos para usar la cámara
     */
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //El usuario ya ha rechazado anteriormente los permisos
            Toast.makeText(this, "Permiso rechazado", Toast.LENGTH_SHORT).show()
        } else {
            //Todavia no ha rechazado ni aceptado -> PEDIR
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMARA_RQ)
        }
    }

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
    }


    /**
     * Abrir la cámara para tomar la foto
     */
    private fun abrirCamara() {

        photoFile = getPhotoFile(FILE_NAME)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
        // Donde guardar la foto
        val fileProvider = FileProvider.getUriForFile(
            this,
            "com.ilerna.pmdm.pacdedesarrollo.fileprovider",
            photoFile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            //Lanzamos la activity para usar la cámara
            resultContract.launch(takePictureIntent)
        } else {
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