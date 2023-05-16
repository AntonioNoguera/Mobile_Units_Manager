package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityFullSizeImageBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class fullSizeImage : AppCompatActivity() {
    private lateinit var binding: ActivityFullSizeImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullSizeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Recibe la imagen, y la undidad
        val unidadEspecificada = intent.getStringExtra("unidadEspecificada")
        val direccionImg = intent.getStringExtra("direccionImg")

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val imagesRef: StorageReference = storageRef.child("Fotos")

        //val imageName = "Testeo-Carroceria.jpg"
        // Obtiene una referencia específica a la imagen utilizando su nombre de archivo
        val imageRef: StorageReference = imagesRef.child(direccionImg.toString())

        // Obtiene la URL de descarga de la imagen
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            val downloadUrl = uri.toString()

            val imageView = findViewById<ImageView>(R.id.targetImg)
            Picasso.get().load(downloadUrl).into(imageView)
        }.addOnFailureListener { exception ->
            // Maneja el error si no se puede obtener la URL de descarga de la imagen
            Toast.makeText(this,"No funciono", Toast.LENGTH_SHORT).show()
        }



        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, imagenes_unidad::class.java)
            intent.putExtra("unidadEspecificada",unidadEspecificada.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }
    }
}