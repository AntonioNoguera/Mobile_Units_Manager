package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivitySolicitudesCuentasBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class solicitudes_cuentas : AppCompatActivity() {
    private lateinit var binding: ActivitySolicitudesCuentasBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolicitudesCuentasBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val arrayPendientes = ArrayList<String>()
        val userPendientes=ArrayList<String>()
        val arrayAutorizados = ArrayList<String>()

        var nUsuarios=0

        val placasRf = db.collection("empleados")
        placasRf.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val empleadosData = document.data
                    if (!(empleadosData["autorizado"] as Boolean)) {
                        arrayPendientes.add(empleadosData["nombreCompleto"] as String)
                        userPendientes.add(document.id)
                        nUsuarios++;
                    } else {
                        arrayAutorizados.add(empleadosData["nombreCompleto"] as String)
                    }
                }

                binding.labelnCuentas.setText("¡Hay "+nUsuarios+" Solicitudes Pendientes!")
            }
        val adapterP = ArrayAdapter(this, R.layout.elemento_lista,arrayPendientes)
        val adapterA = ArrayAdapter(this, R.layout.elemento_lista,arrayAutorizados)

        //Adapter

        binding.dropNueva.setAdapter(adapterP)
        binding.dropExistente.setAdapter(adapterA)

        var selecExistente=""
        var selecNuevo=""

        binding.dropExistente.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
            selecExistente = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this,selecExistente, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, cuenta_existente::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.dropNueva.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
            selecNuevo = userPendientes[i].toString()
            Toast.makeText(this,selecNuevo, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, solicitud_nuevas_cuentas::class.java)
            intent.putExtra("user",selecNuevo)
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