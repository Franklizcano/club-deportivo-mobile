package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)


        // --- ¡AÑADIDO! Lógica para el botón de logout ---
        val btnLogout = findViewById<ImageView>(R.id.iv_logout)
        btnLogout.setOnClickListener {
            mostrarDialogoDeCierreSesion()
        }

        // --- Botones del Menú ---
        val btnRegistro = findViewById<LinearLayout>(R.id.btn_registro)
        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // val btnCarnet = findViewById<LinearLayout>(R.id.btn_carnet)

        val btnCobro = findViewById<LinearLayout>(R.id.btn_pago)
        btnCobro.setOnClickListener {
            val intent = Intent(this, CobroActivity::class.java)
            startActivity(intent)
        }

        val btnCuotas = findViewById<LinearLayout>(R.id.btn_cuotas)
        btnCuotas.setOnClickListener {
            val intent = Intent(this, CuotasVencidasActivity::class.java)
            startActivity(intent)
        }

    }

    private fun mostrarDialogoDeCierreSesion() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_logout_title)
            .setMessage(R.string.dialog_logout_message)
            .setPositiveButton(R.string.dialog_logout_positive) { dialog, which ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton(R.string.dialog_logout_negative) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}