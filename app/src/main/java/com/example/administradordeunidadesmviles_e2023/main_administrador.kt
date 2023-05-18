package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityMainAdministradorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class main_administrador : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdministradorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnEstadoU.setOnClickListener {
            val intent = Intent(this, estado_unidades::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnCuentas.setOnClickListener {
            val intent = Intent(this, solicitudes_cuentas::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnAdministrar.setOnClickListener {
            val intent = Intent(this, administrar_unidades::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnHistorial.setOnClickListener {
            val intent = Intent(this, historial_unidades::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.btnCerrarSesion.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }

    }
}