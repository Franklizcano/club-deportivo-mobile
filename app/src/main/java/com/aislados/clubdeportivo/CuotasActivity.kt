package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.aislados.clubdeportivo.model.CuotaVencida
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CuotasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_cuotas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_cuotas)

        val listaDeCuotas = listOf(
            CuotaVencida("Juan Giordano", "12/05/2025", "12/05/2025", "$ 22,000.00"),
            CuotaVencida("Daniel Ortega", "06/09/2025", "06/09/2025", "$ 25,000.00"),
            CuotaVencida("Carolina Romero", "05/09/2025", "05/09/2025", "$ 25,000.00"),
            CuotaVencida("Belén Lescano", "06/09/2025", "06/09/2025", "$ 25,000.00"),
            CuotaVencida("Marcos Alberdi", "01/10/2025", "01/10/2025", "$ 23,500.00"),
            CuotaVencida("Sofía Rodriguez", "15/08/2025", "15/08/2025", "$ 22,000.00"),
            CuotaVencida("Lucas Martinez", "20/09/2025", "20/09/2025", "$ 25,000.00"),
            CuotaVencida("Valentina Gomez", "10/09/2025", "10/09/2025", "$ 22,000.00")
        )

        val adapter = CuotasAdapter(listaDeCuotas)
        recyclerView.adapter = adapter

        setupFooter()
    }

    private fun setupFooter() {
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