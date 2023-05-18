package com.example.administradordeunidadesmviles_e2023

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityNuevaUnidadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat
import java.util.*


class nueva_unidad : AppCompatActivity() {
    private lateinit var binding: ActivityNuevaUnidadBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaUnidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dropCarroceriaNuevo
        val arrayOpciones = listOf("Perfecto", "Bueno", "Malo", "Muy Malo")

        //Adapter
        val adapter = ArrayAdapter(this, R.layout.elemento_lista, arrayOpciones)

        //Seteo de DropDownMenus

        //Placas
        var SelectCarro = ""
        binding.dropCarroceriaNuevo.setAdapter(adapter)

        binding.dropCarroceriaNuevo.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                SelectCarro = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this, SelectCarro, Toast.LENGTH_SHORT).show()
            }

        binding.btnContinuar.setOnClickListener {
                if(SelectCarro=="" || binding.inputPlacas.text.toString()=="" || binding.inputKilometraje.text.toString()==""){
                    Toast.makeText(this, "Es necesario tener todos los campos completos", Toast.LENGTH_SHORT).show();
                }else{
                    desplegarDialog(SelectCarro);
                }


        }

        binding.btnVolver.setOnClickListener {
            val intent = Intent(this, administrar_unidades::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
        }

    }

    private fun desplegarDialog(selectCarros:String) {
        val dialogBinding = layoutInflater.inflate(R.layout.alertas_para_usuarios, null)
        val myDialog = Dialog(this)

        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(false)

        //Texto del Dialog
        val titulo = dialogBinding.findViewById<TextView>(R.id.tituloAlerta)
        val texto = dialogBinding.findViewById<TextView>(R.id.textoAlerta)

        titulo.text = "Atención"
        texto.text ="Está a punto de declarar una nueva unidad, por favor, asegurese que los datos ingresados son correctos:\n Placas: ${binding.inputPlacas.text}\nCarroceria: ${selectCarros}\nKilometraje: ${binding.inputKilometraje.text} Km"

        //Dimensiones del Dialog
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.50).toInt()

        myDialog.window?.setLayout(width, height)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        myDialog.show()

        //Botones del Dialog
        val BtnClose = dialogBinding.findViewById<Button>(R.id.btnCerrarz)
        BtnClose.setOnClickListener {
            //Cancelar
            myDialog.dismiss()
        }

        val Seguir = dialogBinding.findViewById<Button>(R.id.btnSeguirz)

        Seguir.setOnClickListener {
            //Continuar

            db.collection("automoviles").document(binding.inputPlacas.text.toString()).set(
                hashMapOf(
                    "fechaRegistro" to getFecha(),
                    "horaRegistro" to getHora(),
                    "stateCarroceria" to selectCarros,
                    "stateConductor" to "Sin Conductor",
                    "stateGasolina" to "-",
                    "stateKilometraje" to binding.inputKilometraje.text.toString(),
                    "stateLimpieza" to "-",
                    "stateNeumaticos" to "-",
                    "stateUbicacion" to "En Base",

                    )
            )

            val intent = Intent(this, administrar_unidades::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)

            Toast.makeText(this, "Nueva unidad almacenada con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getHora(): String {

        val dateFormat = SimpleDateFormat(" HH:mm")
        val hora = dateFormat.format(Date())

        return "$hora"
    }

    private fun getFecha(): String {
        var meses = arrayOf(
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )

        var obtencionMes = (SimpleDateFormat("MM").format(Date()).toInt()) - 1

        var mes = meses[obtencionMes]

        var dia = SimpleDateFormat("d").format(Date()).toString()
        var year = SimpleDateFormat("yyyy").format(Date()).toString()

        return "$dia/$mes/$year"
    }
}