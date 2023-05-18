package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityMainEmpleadoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class main_empleado : AppCompatActivity() {
    private lateinit var binding:ActivityMainEmpleadoBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainEmpleadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val usuario = intent.getStringExtra("user")

        val empleadosRef = db.collection("empleados").document(usuario.toString())
        empleadosRef.get()
            .addOnSuccessListener { document ->
                var datoLeido = document.data
                var flag = datoLeido?.get("estaConectado")
                if(flag.toString()=="true"){
                    //Cierre de turno
                    binding.btnInicioFinTurno.text="Cierre de Turno"
                }else{
                    //Inicio
                    binding.btnInicioFinTurno.text="Inicio de Turno"
                }
            }

        binding.textView.text = "Bienvenido,\n"+usuario;

        binding.btnInicioFinTurno.setOnClickListener{
            val intent = Intent(this, inicio_turno_empleado::class.java)
            intent.putExtra("user",usuario)
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