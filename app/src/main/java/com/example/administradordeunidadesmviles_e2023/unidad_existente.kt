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
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityUnidadExistenteBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore

class unidad_existente : AppCompatActivity() {
    private lateinit var binding:ActivityUnidadExistenteBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnidadExistenteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se leen los datos
        val bundle = intent.extras
        val placasEspecificadas = bundle?.getString("placas")

        binding.lblPlacas.text = placasEspecificadas
        val usuariosRef = db.collection("automoviles").document(placasEspecificadas.toString())

        usuariosRef.get()
            .addOnSuccessListener { document ->
                var datoLeido = document.data
                binding.lblCarro.text = datoLeido?.get("stateCarroceria") as CharSequence?
                binding.lblKilometraje.text = datoLeido?.get("stateKilometraje") as CharSequence?
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Placas no encontradas", Toast.LENGTH_SHORT).show()
            }



        binding.btnEliminarUnidad.setOnClickListener{

            val dialogBinding=layoutInflater.inflate(R.layout.alertas_para_usuarios,null)
            val myDialog = Dialog(this)

            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(false)

            //Texto del Dialog
            val titulo = dialogBinding.findViewById<TextView>(R.id.tituloAlerta)
            val texto = dialogBinding.findViewById<TextView>(R.id.textoAlerta)

            titulo.text="¡Atención!"
            texto.text="Si continuas con esta acción la unidad, serán eliminada por completo, y junto con ella todos los registros que se han realizado, ¿está seguro de continuar?"

            //Dimensiones del Dialog
            val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.42).toInt()

            myDialog.window?.setLayout(width, height)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            myDialog.show()

            //Botones del Dialog
            val BtnClose = dialogBinding.findViewById<Button>(R.id.btnCerrarz)
            BtnClose.setOnClickListener{
                //Cancelar
                myDialog.dismiss()
            }

            val Seguir = dialogBinding.findViewById<Button>(R.id.btnSeguirz)

            Seguir.setOnClickListener{
                db.collection("automoviles").document(placasEspecificadas.toString())
                    .delete()
                    .addOnSuccessListener { Toast.makeText(this,"Unidad Correctamente eliminada", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this,"Hubo un problema con el proceso", Toast.LENGTH_SHORT).show() }

                val intent = Intent(this, administrar_unidades::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)


            }



        }

        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, administrar_unidades::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }
    }
}