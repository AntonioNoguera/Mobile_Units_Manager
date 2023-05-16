package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityDetallesAvanzadosBinding
import com.google.firebase.firestore.FirebaseFirestore

class detalles_avanzados : AppCompatActivity() {
    private lateinit var binding:ActivityDetallesAvanzadosBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesAvanzadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se leen los cambios del tipo de cuenta
        val bundle = intent.extras

        val unidadEspecificada = bundle?.getString("placas")

        val unidadesRef = db.collection("automoviles").document(unidadEspecificada.toString())

        unidadesRef.get()
            .addOnSuccessListener { document ->
                var datoLeido = document.data
                binding.txtUnidad.text = document.id
                var Dia = datoLeido?.get("fechaRegistro") as CharSequence?
                var Hora = datoLeido?.get("horaRegistro") as CharSequence?
                binding.txtFecha.text = "Fecha: "+Dia+" / "+Hora
                binding.txtNeumaticos.text = datoLeido?.get("stateNeumaticos") as CharSequence?
                binding.txtCarroceria.text = datoLeido?.get("stateCarroceria") as CharSequence?
                binding.txtLimpieza.text = datoLeido?.get("stateLimpieza") as CharSequence?
                binding.txtKilometraje.text = datoLeido?.get("stateKilometraje") as CharSequence?

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Placas no encontradas", Toast.LENGTH_SHORT).show()
            }


        binding.btnDetalles.setOnClickListener{
            val intent = Intent(this, imagenes_unidad::class.java)
            intent.putExtra("unidadEspecificada",unidadEspecificada.toString())

            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, estado_unidades::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }
    }
}