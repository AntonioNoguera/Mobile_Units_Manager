package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityImagenesUnidadBinding
import com.google.firebase.firestore.FirebaseFirestore


import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.squareup.picasso.Picasso

import java.io.File


class imagenes_unidad : AppCompatActivity() {
    private lateinit var binding: ActivityImagenesUnidadBinding
    private val db = FirebaseFirestore.getInstance()

    companion object {
        var direccion: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagenesUnidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val unidadEspecificada = intent.getStringExtra("unidadEspecificada")

        val unidadesRef = db.collection("automoviles").document(unidadEspecificada.toString())

        unidadesRef.get()
            .addOnSuccessListener { document ->
                var datoLeido = document.data
                binding.txtUnidad.text = document.id
                var Dia = datoLeido?.get("fechaRegistro") as CharSequence?
                var Hora = datoLeido?.get("horaRegistro") as CharSequence?

                binding.txtFecha.text = "Fecha: "+Dia+" / "+Hora
                binding.txtUnidad.text=unidadEspecificada

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Placas no encontradas", Toast.LENGTH_SHORT).show()
            }



        var UnidadActual=0

        val imagenesDisponibles=listOf("Imagen de Interiores","Estado de las llantas","Estado de Carroceria","Estado de Tablero")
        val tituImagen=listOf("Interiores","Llantas","Carroceria","Tablero")

        fun verificador(){
            binding.lblImageActual.text=imagenesDisponibles[UnidadActual]
            binding.anterior.isEnabled = UnidadActual != 0
            binding.siguiente.isEnabled = UnidadActual != 3


            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            val imagesRef: StorageReference = storageRef.child("Fotos")

            val imageName = unidadEspecificada+"-"+tituImagen[UnidadActual]+".png"
            //val imageName = "Testeo-Carroceria.jpg"
            // Obtiene una referencia específica a la imagen utilizando su nombre de archivo
            val imageRef: StorageReference = imagesRef.child(imageName)

            // Obtiene la URL de descarga de la imagen
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                direccion = unidadEspecificada+"-"+tituImagen[UnidadActual]+".png"

                val imageView = findViewById<ImageView>(R.id.targetImg)
                Picasso.get().load(downloadUrl).into(imageView)
            }.addOnFailureListener { exception ->
                // Maneja el error si no se puede obtener la URL de descarga de la imagen
                Toast.makeText(this,"No funciono",Toast.LENGTH_SHORT).show()
            }



        }

        binding.siguiente.setOnClickListener{
            UnidadActual++
            verificador()
        }

        binding.anterior.setOnClickListener {
            UnidadActual--
            verificador()
        }

        verificador()

        binding.targetImg.setOnClickListener{
            val intent = Intent(this, fullSizeImage::class.java)
            intent.putExtra("unidadEspecificada",unidadEspecificada.toString())
            intent.putExtra("direccionImg",direccion)

            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, detalles_avanzados::class.java)
            intent.putExtra("placas",unidadEspecificada.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }
    }
}