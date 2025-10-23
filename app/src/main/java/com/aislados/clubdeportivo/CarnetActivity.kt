package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        // Encontrar los componentes del layout
        val tilDni = findViewById<TextInputLayout>(R.id.til_dni_carnet)
        val etDni = findViewById<TextInputEditText>(R.id.et_dni_carnet)
        val etNombre = findViewById<TextInputEditText>(R.id.et_nombre_carnet)
        val etApellido = findViewById<TextInputEditText>(R.id.et_apellido_carnet)
        val etEstado = findViewById<TextInputEditText>(R.id.et_estado_carnet)
        val btnEmitirCarnet = findViewById<MaterialButton>(R.id.btn_emitir_carnet)

        // --- LÓGICA DE BÚSQUEDA ---
        // Se ejecuta cuando el usuario toca el ícono de la lupa
        tilDni.setEndIconOnClickListener {
            val dni = etDni.text.toString()
            if (dni.isNotEmpty()) {
                Toast.makeText(this, "Buscando DNI: $dni...", Toast.LENGTH_SHORT).show()

                // --- SIMULACIÓN DE BÚSQUEDA EN BASE DE DATOS ---
                // En un caso real, aquí se llamarías a Socio tras buscar el DNI.
                // Por ahora, solo rellenamos con datos de ejemplo.
                etNombre.setText("Juan")
                etApellido.setText("Perez")
                etEstado.setText("Activo")
                // ---------------------------------------------------

            } else {
                Toast.makeText(this, "Por favor, ingrese un DNI", Toast.LENGTH_SHORT).show()
            }
        }

        // --- LÓGICA DEL BOTÓN DE EMITIR CARNET ---
        btnEmitirCarnet.setOnClickListener {
            // Aquí irá la lógica para generar o marcar la entrega del carnet
            Toast.makeText(this, "Emitiendo carnet...", Toast.LENGTH_SHORT).show()
        }

        // --- LÓGICA PARA LOS BOTONES DEL FOOTER ---
        val btnAtras = findViewById<LinearLayout>(R.id.btn_atras)
        val btnMenuPrincipal = findViewById<LinearLayout>(R.id.btn_menu_principal)
        val btnCerrarSesion = findViewById<LinearLayout>(R.id.btn_cerrar_sesion)

        btnAtras.setOnClickListener { finish() }

        btnMenuPrincipal.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}