package com.example.administradordeunidadesmviles_e2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityInicioTurnoEmpleadoBinding
import android.widget.Toast
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class inicio_turno_empleado : AppCompatActivity() {
    private lateinit var binding:ActivityInicioTurnoEmpleadoBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioTurnoEmpleadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getStringExtra("user")

        //Se declara la altura del container

        val heightA = (resources.displayMetrics.heightPixels * 0.50).toInt()
        val layo = binding.contenedor.layoutParams

        layo.height=heightA

        binding.contenedor.layoutParams=layo




        fun aparecerElementos(){

            binding.NeumaticosBox.visibility = View.VISIBLE
            binding.lblNeumaticos.visibility = View.VISIBLE

            binding.LimpiezaBox.visibility = View.VISIBLE
            binding.lblLimpieza.visibility = View.VISIBLE

            binding.CarroceriaBox.visibility = View.VISIBLE
            binding.lblCarroceria.visibility = View.VISIBLE

            binding.lblGasolina.visibility = View.VISIBLE
            binding.inputKilometraje.visibility = View.VISIBLE

            binding.lblNivel.visibility = View.VISIBLE
            binding.sliderGas.visibility = View.VISIBLE
        }

        //Arreglos

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

        var SelectPlacas=""
        binding.dropPlacas.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->

            SelectPlacas = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this,SelectPlacas,Toast.LENGTH_SHORT).show()

            //Linea para hacer que aparezcan todos los demas drops
            aparecerElementos()
        }
        //Neumaticos
        var EstadoNeumaticos=""


        //dropCarroceriaNuevo
        val arrayOpcionesAlter = listOf("Perfecto", "Bueno", "Malo", "Muy Malo")

        //Adapter
        val adapterAlter = ArrayAdapter(this, R.layout.elemento_lista, arrayOpcionesAlter)

        //dropCarroceriaNuevo
        val arrayOpcionesNeu = listOf("Presión Perfecta", "Presión Normal", "Presión Baja", "Daño Inutilizable")

        //Adapter
        val adapterNeu = ArrayAdapter(this, R.layout.elemento_lista, arrayOpcionesNeu)

        binding.dropNeumatico.setAdapter(adapterNeu)

        binding.dropNeumatico.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->

            EstadoNeumaticos = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this,EstadoNeumaticos,Toast.LENGTH_SHORT).show()
        }

        //Carroceria
        var EstadoCarroceria=""
        binding.dropCarroceria.setAdapter(adapterAlter)

        binding.dropCarroceria.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->

            EstadoCarroceria = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this,EstadoCarroceria,Toast.LENGTH_SHORT).show()
        }

        //Limpieza

        var EstadoLimpieza=""
        binding.dropLimpieza.setAdapter(adapterAlter)

        binding.dropLimpieza.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->

            EstadoLimpieza = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this,EstadoLimpieza,Toast.LENGTH_SHORT).show()
        }

        //Movilidad
        binding.btnContinuar.setOnClickListener{

            //Aqui es donde va el post
            if(EstadoNeumaticos=="" || EstadoCarroceria=="" || EstadoLimpieza=="" || binding.inputKilometraje.text.toString()==""){
                Toast.makeText(this,"No puedes dejar campos vacios.",Toast.LENGTH_SHORT).show()
            }else{
                //Código del insert


                db.collection("automoviles").document(SelectPlacas).set(
                    hashMapOf(
                        "fechaRegistro" to getFecha(),
                        "horaRegistro" to getHora(),
                        "stateCarroceria" to EstadoCarroceria,
                        "stateConductor" to usuario,
                        "stateGasolina" to (binding.sliderGas.value.toInt()).toString()+"/10",
                        "stateKilometraje" to binding.inputKilometraje.text.toString(),
                        "stateLimpieza" to EstadoLimpieza,
                        "stateNeumaticos" to EstadoNeumaticos,
                        "stateUbicacion" to "En Turno",

                        )
                )

                val empleadosRef = db.collection("empleados").document(usuario.toString())
                empleadosRef.get()
                    .addOnSuccessListener { document ->
                        var datoLeido = document.data

                        var flag = datoLeido?.get("estaConectado")
                        if(flag.toString()=="true"){
                            //Cierre de turno
                            val data = hashMapOf(
                                "estaConectado" to "false"
                            )
                            if (usuario != null) {
                                db.collection("empleados").document(usuario)
                                    .update(data as Map<String, Any>)
                            }
                        }else{
                            //Inicio
                             val data = hashMapOf(
                                "estaConectado" to "true"
                            )
                            if (usuario != null) {
                                db.collection("empleados").document(usuario)
                                    .update(data as Map<String, Any>)
                            }
                        }
                    }



                val intent = Intent(this, captura_de_imagenes::class.java)
                intent.putExtra("user",usuario)
                intent.putExtra("unidad",SelectPlacas)
                intent.putExtra("nImagen","0")
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }

        }

        binding.btnVolver.setOnClickListener{
            val intent = Intent(this, main_empleado::class.java)
            intent.putExtra("user",usuario)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
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