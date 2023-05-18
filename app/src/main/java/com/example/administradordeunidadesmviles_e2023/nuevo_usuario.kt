package com.example.administradordeunidadesmviles_e2023

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityNuevoUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class nuevo_usuario : AppCompatActivity() {
    private lateinit var binding:ActivityNuevoUsuarioBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener {

            if(binding.inputNombre.text.toString()=="" || binding.inputUser.text.toString()=="" || binding.inputContra.text.toString()=="" || binding.inputVerificar.toString()=="" ){

                Toast.makeText(this,"Todos los campos deben de estar completos", Toast.LENGTH_SHORT).show()
            }else {
                if(binding.inputContra.text.toString() != binding.inputVerificar.text.toString()){
                    Toast.makeText(this,"Ambas contraseñas deberán de coincidir.", Toast.LENGTH_SHORT).show()
                }else{
                    insertDatos()

                    //Aqui es donde va el dialogo

                    //ALERTAS PARA USUARIO PT 2
                    val dialogBinding = layoutInflater.inflate(R.layout.solo_continuar, null)
                    val myDialog = Dialog(this)

                    myDialog.setContentView(dialogBinding)
                    myDialog.setCancelable(false)

                    //Texto del Dialog
                    val titulo = dialogBinding.findViewById<TextView>(R.id.tituloAlerta)
                    val texto = dialogBinding.findViewById<TextView>(R.id.textoAlerta)

                    titulo.text = "Listo"
                    texto.text = "Has completado el proceso para crear tu cuenta, comentale a tu superior, y espera a que autorizen tu cuenta."

                    //Dimensiones del Dialog
                    val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
                    val height = (resources.displayMetrics.heightPixels * 0.40).toInt()

                    myDialog.window?.setLayout(width, height)
                    myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    myDialog.show()

                    val Seguir = dialogBinding.findViewById<Button>(R.id.btnSeguirz)

                    Seguir.setOnClickListener {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)

                        Toast.makeText(this, "Solicitud realizada con éxito", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_up_in, R.anim.from_down_out)
        }
    }

    private fun insertDatos() {
        db.collection("empleados").document(binding.inputUser.text.toString()).set(
            hashMapOf(
                "password" to binding.inputContra.text.toString(),
                "nombreCompleto" to binding.inputNombre.text.toString(),
                "autorizado" to false,
                "estaConectado" to false,
                "esAdministrador" to false
            )
        )

    }
}