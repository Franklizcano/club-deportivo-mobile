package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val btnSocio = findViewById<Button>(R.id.btn_socio)

        btnSocio.setOnClickListener {
            // Crea un Intent para ir de RegistroActivity a AltaSocioActivity
            val intent = Intent(this, AltaSocioActivity::class.java)
            startActivity(intent)
        }

        val btnNoSocio = findViewById<Button>(R.id.btn_no_socio)

        btnNoSocio.setOnClickListener {
            val intent = Intent(this, AltaNoSocioActivity::class.java)
            startActivity(intent)
        }
    }

}