package com.aislados.clubdeportivo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegistro = findViewById<LinearLayout>(R.id.btn_registro)

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Botón Carnet
        val btnCarnet = findViewById<LinearLayout>(R.id.btn_carnet)
        btnCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)
        }

        // Botón "Cobro"
        val btnCobro = findViewById<LinearLayout>(R.id.btn_pago)
        btnCobro.setOnClickListener {
            val intent = Intent(this, CobroActivity::class.java)
            startActivity(intent)
        }

        // Botón "Cuotas"
        val btnCuotas = findViewById<LinearLayout>(R.id.btn_cuotas)
        btnCuotas.setOnClickListener {
            val intent = Intent(this, CuotasActivity::class.java)
            startActivity(intent)
        }


    }
}