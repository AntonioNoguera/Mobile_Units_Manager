package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityEstadoUnidadesBinding
import com.google.firebase.firestore.FirebaseFirestore

class estado_unidades : AppCompatActivity() {
    private lateinit var binding:ActivityEstadoUnidadesBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadoUnidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Arranca SELECT
        //

        val placasRf = db.collection("automoviles")
        var nAutosF=0
        placasRf.get()
            .addOnSuccessListener { querySnapshot ->
                val miMapa = hashMapOf<Int, MutableList<String>>()
                for (document in querySnapshot) {

                    var datoLeido = document.data

                    miMapa.getOrPut(nAutosF) { mutableListOf() }.add(document.id)

                    miMapa.getOrPut(nAutosF) { mutableListOf() }.add((datoLeido?.get("stateConductor") as String).toString())
                    miMapa.getOrPut(nAutosF) { mutableListOf() }.add((datoLeido?.get("stateUbicacion") as String).toString())
                    miMapa.getOrPut(nAutosF) { mutableListOf() }.add((datoLeido?.get("stateGasolina") as String).toString())

                    nAutosF++
                }

                binding.unidadActual.text="Unidades disponibles: 1/"+nAutosF

                var UnidadActual=1

                fun verificador(){
                    binding.anterior.isEnabled = UnidadActual != 1
                    binding.siguiente.isEnabled = UnidadActual != nAutosF
                    binding.unidadActual.text="Unidades disponibles: "+UnidadActual+"/"+nAutosF.toString()


                    //Codigo que seteara cada cosa en su lugar

                    val valoresUnidadActual=miMapa[UnidadActual-1]

                    binding.txtPlacas.text=valoresUnidadActual?.get(0).toString()
                    binding.txtChofer.text=valoresUnidadActual?.get(1).toString()
                    binding.txtUbicacion.text=valoresUnidadActual?.get(2).toString()
                    binding.txtGasolina.text=valoresUnidadActual?.get(3).toString()

                }

                binding.siguiente.setOnClickListener{
                    UnidadActual += 1
                    verificador()
                }


                binding.anterior.setOnClickListener {
                    UnidadActual -= 1
                    verificador()
                }

                verificador()


            }



        binding.btnDetalles.setOnClickListener{
            val intent = Intent(this, detalles_avanzados::class.java)
            intent.putExtra("placas",binding.txtPlacas.text)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, main_administrador::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }
    }
}