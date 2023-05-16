package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityAdministrarUnidadesBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore

class administrar_unidades : AppCompatActivity() {
    private lateinit var binding:ActivityAdministrarUnidadesBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministrarUnidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Se genera el arreglo de placas
        val arrayOpciones = ArrayList<String>()

        val placasRf = db.collection("automoviles")
        placasRf.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    arrayOpciones.add(document.id.toString())
                }
            }
            .addOnFailureListener { exception ->
            }

        val adapter = ArrayAdapter(this, R.layout.elemento_lista,arrayOpciones)

        binding.dropPlacas.setAdapter(adapter)

        var selectPlacas = ""
        binding.dropPlacas.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->

            selectPlacas = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this,selectPlacas, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, unidad_existente::class.java)
            intent.putExtra("placas",selectPlacas)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnEstadoU.setOnClickListener{
            val intent = Intent(this, nueva_unidad::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnCerrarSesion.setOnClickListener{
            val intent = Intent(this, main_administrador::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }
    }
}