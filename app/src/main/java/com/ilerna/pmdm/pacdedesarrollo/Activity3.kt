package com.ilerna.pmdm.pacdedesarrollo

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.ilerna.pmdm.pacdedesarrollo.databinding.Activity3Binding
import java.io.File

private lateinit var photoFile: File
private const val FILE_NAME = "photo.jpg"

class Activity3 : AppCompatActivity(), Aux {

    private lateinit var binding: Activity3Binding
    private val resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //LO DE LA CAMARA, PERO NO CAMARA2
                //val takenImage = result.data?.extras?.get("data") as Bitmap
                //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                //binding.ivFoto.setImageBitmap(takenImage)
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

        //Obtenemos el nombre enviado desde la Activity 1
        intent.extras?.getString("nombre")?.let { dialogobienvenida(this, it) }

        /*//Obtenemos el nombre enviado desde la Activity 1
        val dialogString = intent.extras?.getString("nombre")

        //Dialogo para informar en qué Activity estamos
        val dialogo = AlertDialog.Builder(this)
        dialogo.setMessage("Bienvenida/o a la Activity 3, $dialogString")
            .setPositiveButton(R.string.aceptar) { _, _ ->
                //No hacemos nada, solo aceptar para cerrar el dialogo
            }.create().show()*/

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
            //Lanzamos un Toast para informar
            Toast.makeText(this, R.string.camara_lanzada, Toast.LENGTH_SHORT).show()

            //LO DE LA CAMARA, PERO NO CAMARA2
            //val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //photoFile = getPhotoFile(FILE_NAME)
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            //val fileProvider = FileProvider.getUriForFile(this, "com.ilerna.pmdm.fileprovider", photoFile)
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            //if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            //    resultContract.launch(takePictureIntent)
            //} else {
            Toast.makeText(this, "No se puede abrir la cámara", Toast.LENGTH_SHORT).show()
            //}

        }//binding.btnCamera.setOnClickListener

    }//onCreate

    /**
     *
     */
    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

}//Activity3