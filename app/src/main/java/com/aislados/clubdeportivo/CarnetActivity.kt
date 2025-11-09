package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
// import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carnet)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val ivFotoSocio = findViewById<ImageView>(R.id.iv_foto_socio) // NUEVO
        val etNroSocio = findViewById<TextInputEditText>(R.id.et_nro_socio) // NUEVO
        val etDni = findViewById<TextInputEditText>(R.id.et_dni_carnet) // Existía
        val etNombre = findViewById<TextInputEditText>(R.id.et_nombre_carnet) // Existía
        val etApellido = findViewById<TextInputEditText>(R.id.et_apellido_carnet) // Existía
        val etEstado = findViewById<TextInputEditText>(R.id.et_estado_carnet) // Existía

        // Vistas ELIMINADAS (ya no se buscan)
        // val tilDni = findViewById<TextInputLayout>(R.id.til_dni_carnet) // BORRADO
        // val btnEmitirCarnet = findViewById<MaterialButton>(R.id.btn_emitir_carnet) // BORRADO


        // --- LÓGICA DE BÚSQUEDA (ELIMINADA) ---
        // 5. Borramos la lógica que usaba los IDs eliminados
        // tilDni.setEndIconOnClickListener { ... }
        // btnEmitirCarnet.setOnClickListener { ... }


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
            mostrarDialogoDeCierreSesion()
        }
    }
    private fun mostrarDialogoDeCierreSesion() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_logout_title)
            .setMessage(R.string.dialog_logout_message)

            // Botón "Sí, Cerrar" (Positivo)
            .setPositiveButton(R.string.dialog_logout_positive) { dialog, which ->
                // --- ¡Aquí va tu código original de logout! ---
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                // ---------------------------------------------
            }

            // Botón "Cancelar" (Negativo)
            .setNegativeButton(R.string.dialog_logout_negative) { dialog, which ->
                // Simplemente cierra el diálogo y no hace nada
                dialog.dismiss()
            }
            .show()
    }
}