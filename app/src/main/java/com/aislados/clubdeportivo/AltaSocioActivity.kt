package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AltaSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_socio)

        val etFechaNacimiento = findViewById<TextInputEditText>(R.id.et_fecha_nacimiento)

        etFechaNacimiento.isFocusable = false
        etFechaNacimiento.isClickable = true

        etFechaNacimiento.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()

            datePicker.addOnPositiveButtonClickListener { utcMillis ->
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(utcMillis)
                val localDate = Date(utcMillis + offset)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("es", "AR"))
                etFechaNacimiento.setText(sdf.format(localDate))
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }


        val btnAtras = findViewById<LinearLayout>(R.id.btn_atras)
        val btnMenuPrincipal = findViewById<LinearLayout>(R.id.btn_menu_principal)
        val btnCerrarSesion = findViewById<LinearLayout>(R.id.btn_cerrar_sesion)

        // Asignación de acción a cada botón

        // Funcionalidad para "Atrás"
        btnAtras.setOnClickListener {
            // Cierra la actividad actual y regresa a la pantalla anterior.
            finish()
        }

        // Funcionalidad para "Menú Principal"
        btnMenuPrincipal.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            // Limpia las pantallas que estén por encima del Menú Principal.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        // Funcionalidad para "Cerrar Sesión"
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            // Borra todo el historial y empieza de nuevo en el Login.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}