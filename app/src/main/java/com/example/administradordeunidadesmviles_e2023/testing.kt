package com.example.administradordeunidadesmviles_e2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.administradordeunidadesmviles_e2023.databinding.ActivityTestingBinding
//import com.google.firebase.auth.FirebaseAuth

import java.text.SimpleDateFormat
import java.util.*

class testing : AppCompatActivity() {
    private lateinit var binding: ActivityTestingBinding
    //private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)





            val dateFormat = SimpleDateFormat("d MM yyyy, HH:mm")
            val date = dateFormat.format(Date())

            Toast.makeText(this,date,Toast.LENGTH_SHORT).show()
        }
}