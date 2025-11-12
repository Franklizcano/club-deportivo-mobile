package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aislados.clubdeportivo.database.AppDatabase
import com.aislados.clubdeportivo.extensions.parcelable
import com.aislados.clubdeportivo.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

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

        val user = intent.parcelable<User>("USER")

        val ivFotoSocio = findViewById<ImageView>(R.id.iv_foto_socio)
        val etNroSocio = findViewById<TextInputEditText>(R.id.et_nro_socio)
        val etDni = findViewById<TextInputEditText>(R.id.et_dni_carnet)
        val etNombre = findViewById<TextInputEditText>(R.id.et_nombre_carnet)
        val etApellido = findViewById<TextInputEditText>(R.id.et_apellido_carnet)
        val etEstado = findViewById<TextInputEditText>(R.id.et_estado_carnet)

        val database = AppDatabase.getDatabase(this)
        val socioDao = database.socioDao()
        val socio = socioDao.findSocioById(user?.socioId ?: 0)
        val estadoSocio = socioDao.tieneCuotaVencida(user?.socioId ?: 0)

        etNroSocio.setText(socio?.id.toString())
        etDni.setText(socio?.dni.toString())
        etNombre.setText(socio?.nombre.toString())
        etApellido.setText(socio?.apellido.toString())
        etEstado.setText(if (estadoSocio) "En mora" else "Activo")

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