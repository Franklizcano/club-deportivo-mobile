package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.aislados.clubdeportivo.model.CuotaVencida

class CuotasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuotas)

        // 1. Encontrar el RecyclerView en el layout
        val recyclerView = findViewById<RecyclerView>(R.id.rv_cuotas)

        // 2. Crear la lista de datos de ejemplo (como pediste)
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

        // 3. Crear una instancia del Adaptador y pasársela al RecyclerView
        val adapter = CuotasAdapter(listaDeCuotas)
        recyclerView.adapter = adapter

        // 4. Configurar la lógica del footer
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
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}