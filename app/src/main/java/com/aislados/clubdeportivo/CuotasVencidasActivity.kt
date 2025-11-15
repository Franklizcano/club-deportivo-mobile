package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.aislados.clubdeportivo.database.AppDatabase
import com.aislados.clubdeportivo.model.CuotaVencida
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class CuotasVencidasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etBuscarSocio: TextInputEditText
    private lateinit var adapter: CuotasAdapter
    private var listaDeCuotasCompleta: List<CuotaVencida> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_cuotas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.rv_cuotas)
        etBuscarSocio = findViewById(R.id.et_buscar_socio)
        listaDeCuotasCompleta = obtenerCuotasVencidas() ?: emptyList()
        adapter = CuotasAdapter(listaDeCuotasCompleta)
        recyclerView.adapter = adapter

        setupSearchListener()
        setupFooter()
    }

    private fun setupSearchListener() {
        etBuscarSocio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterList(s?.toString() ?: "")
            }
        })
    }

    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            listaDeCuotasCompleta
        } else {
            listaDeCuotasCompleta.filter { cuota ->
                cuota.nombre.contains(query, ignoreCase = true)
            }
        }
        adapter.updateList(filteredList)
    }

    private fun obtenerCuotasVencidas(): List<CuotaVencida>? {
        val db = AppDatabase.getDatabase(this)
        val cuotaDao = db.cuotaDao()
        val socioDao = db.socioDao()

        val cuotasVencidas = cuotaDao.getCuotasBySocioIdLastExpired(LocalDate.now())

        return cuotasVencidas.mapNotNull { cuota ->
            cuota.socioId?.let { socioId ->
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