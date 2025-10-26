package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.aislados.clubdeportivo.database.AppDatabase
import com.aislados.clubdeportivo.model.CuotaVencida
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CuotasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuotas)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_cuotas)
        val listaDeCuotas = obtenerCuotasVencidas()
        val adapter = CuotasAdapter(listaDeCuotas)
        recyclerView.adapter = adapter
        setupFooter()
    }

    private fun obtenerCuotasVencidas(): List<CuotaVencida> {
        val db = AppDatabase.getDatabase(this)
        val cuotaDao = db.cuotaDao()
        val socioDao = db.socioDao()

        val cuotasVencidas = cuotaDao.getAllCuotas().filter { cuota ->
            cuota.fechaVencimiento.isBefore(LocalDate.now())
        }

        return cuotasVencidas.mapNotNull { cuota ->
            cuota.socioId.let { socioId ->
                socioDao.findSocioById(socioId)?.let { socio ->
                    CuotaVencida(
                        nombre = "${socio.nombre} ${socio.apellido}",
                        fechaPago = cuota.fechaPago.toString(),
                        fechaVencimiento = cuota.fechaVencimiento.toString(),
                        monto = "$ ${cuota.monto}"
                    )
                }
            }
        }
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