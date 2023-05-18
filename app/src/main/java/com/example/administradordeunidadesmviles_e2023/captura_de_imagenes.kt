package com.example.administradordeunidadesmviles_e2023

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityCapturaDeImagenesBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class captura_de_imagenes : AppCompatActivity() {
    private lateinit var binding:ActivityCapturaDeImagenesBinding
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    companion object {
        var unidad: String = ""
        var titulo: String = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturaDeImagenesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnCamara = findViewById<Button>(R.id.btnCapturar)





        val usuario = intent.getStringExtra("user")
        unidad = intent.getStringExtra("unidad").toString()
        val nImagen= intent.getStringExtra("nImagen")

        Toast.makeText(this,unidad.toString(),Toast.LENGTH_SHORT).show()

        val imagenesDisponibles=listOf("Estado de Interiores","Estado de las llantas","Estado de Carroceria","Estado de Tablero")
        val tituImagen=listOf("Interiores","Llantas","Carroceria","Tablero")

        if (nImagen != null) {
            binding.tituloImg.text=imagenesDisponibles[nImagen.toInt()]
            titulo=tituImagen[nImagen.toInt()].toString()

        }

        btnCamara.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // Agrega el putExtra con la clave y valor que deseas enviar
            takePictureIntent.putExtra("unidad",unidad.toString())

            startForResult.launch(takePictureIntent)
        }

        binding.btnContinuar.setOnClickListener{

            if (nImagen != null) {
                if(nImagen.toInt()==3){
                    val intent = Intent(this, main_empleado::class.java)
                    intent.putExtra("user",usuario)
                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                }else{
                    val intent = Intent(this, captura_de_imagenes::class.java)
                    intent.putExtra("user",usuario)
                    intent.putExtra("unidad",unidad)
                    var nIteracion =  nImagen.toInt()+1
                    intent.putExtra("nImagen",nIteracion.toString())

                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)

                }
            }



        }


    }

     val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            val imageBitmap =  intent?.extras?.get("data") as Bitmap
            val imageView3 = findViewById<ImageView>(R.id.imageView3)
            imageView3.setImageBitmap(imageBitmap)

            val timestamp = System.currentTimeMillis()

            //
            Toast.makeText(this,unidad.toString(),Toast.LENGTH_SHORT).show()
            val nombreArchivo = unidad+"-"+titulo+".png"

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
            val referenciaImagen = storageRef.child("Fotos/$nombreArchivo")
            referenciaImagen.putBytes(data)
                .addOnSuccessListener { taskSnapshot ->
                    // la imagen se cargó exitosamente
                    // puedes obtener la URL de descarga con taskSnapshot.metadata?.reference?.downloadUrl
                }
                .addOnFailureListener { exception ->
                    // hubo un error al cargar la imagen
                }

        }


    }

}