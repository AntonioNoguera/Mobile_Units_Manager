package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityMainBinding
import com.example.administradordeunidadesmviles_e2023.ValoresGlobales

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore

private lateinit var firebaseAuth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()

    private fun verificarU():Any{

        val usuariosRef = db.collection("empleados").document(binding.TxtUser.text.toString())
        Log.d("ERRORES",binding.TxtUser.text.toString())

        usuariosRef.get()
            .addOnSuccessListener { document ->

                if (document.exists()) {
                    var datoLeido = document.data

                    var password = datoLeido?.get("password") as String
                    var autorizado = datoLeido?.get("autorizado") as Boolean
                    var permisos = datoLeido?.get("esAdministrador") as Boolean

                    if(password==binding.txtContra.text.toString()){
                        //Contraseña y usuario coinciden

                        if(autorizado){
                            val Usuario = ValoresGlobales()
                            Usuario.setNombreUser(binding.TxtUser.text.toString())

                            if(permisos){
                                val intent = Intent(this, main_administrador::class.java)
                                intent.putExtra("user",binding.TxtUser.text.toString())
                                startActivity(intent)
                                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                            }else{
                                val intent = Intent(this, main_empleado::class.java)
                                intent.putExtra("user",binding.TxtUser.text.toString())
                                startActivity(intent)
                                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                            }

                        }else{
                            //La cuenta aun no ha sido autorizada
                            Toast.makeText(this,"La cuenta aún no ha sido autorizada, consulte a su superior.",Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        //Contraseña Incorrecta
                        Toast.makeText(this,"La contraseña introducida es incorrecta, verifique nuevamente",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // El documento no existe
                    Toast.makeText(this,"El usuario introducido no existe, consulte con su superior",Toast.LENGTH_SHORT).show()
                }
            }
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener{
            //Validación del usuario
            if(binding.TxtUser.text.toString() == "" || binding.txtContra.text.toString()==""){
                Toast.makeText(this,"No puede dejar campos vacios, ingrese todos sus datos",Toast.LENGTH_SHORT).show();
            }else{
                verificarU()
            }


        }

        binding.txtNuevaCuenta.setOnClickListener {
            val intent = Intent(this, nuevo_usuario::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_down_in, R.anim.from_up_out)
        }
    }




}