package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aislados.clubdeportivo.database.AppDatabase
import com.aislados.clubdeportivo.model.NoSocio
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AltaNoSocioActivity : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var etDomicilio: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnRegistrar: MaterialButton

    val database = AppDatabase.getDatabase(this)
    val noSocioDao = database.noSocioDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_no_socio)

        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        etDni = findViewById(R.id.et_dni)
        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento)
        etDomicilio = findViewById(R.id.et_domicilio)
        etTelefono = findViewById(R.id.et_telefono)
        etEmail = findViewById(R.id.et_email)
        btnRegistrar = findViewById(R.id.btn_registrar_alta_no_socio)

        etFechaNacimiento.isFocusable = false
        etFechaNacimiento.isClickable = true

        setupFooter()

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

        btnRegistrar.setOnClickListener {
            val noSocio = validateNoSocioFields()

            if (noSocio != null) {
                val intent = Intent(this, CobroActivity::class.java)
                intent.putExtra("NO_SOCIOS", noSocio)
                Toast.makeText(this, "Solicitamos el cobro por inscripción", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateNoSocioFields(): NoSocio? {
        val camposTexto = listOf(etNombre, etApellido, etFechaNacimiento, etDomicilio, etTelefono, etEmail)
        val camposValidos = validateFields(camposTexto)
        val dniInt = validateDni()

        return if (camposValidos && dniInt != null) {
            NoSocio(
                nombre = etNombre.text.toString(),
                apellido = etApellido.text.toString(),
                dni = dniInt,
                fechaNacimiento = LocalDate.parse(etFechaNacimiento.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                domicilio = etDomicilio.text.toString(),
                telefono = etTelefono.text.toString(),
                email = etEmail.text.toString()
            )
        } else null
    }

    private fun validateDni(): Int? {
        val textInputLayout = etDni.parent.parent as? TextInputLayout
        val dniText = etDni.text.toString()

        if (dniText.isBlank()) {
            textInputLayout?.error = "Este campo es obligatorio"
            return null
        }

        val dniInt = dniText.toIntOrNull()
        if (dniInt == null) {
            textInputLayout?.error = "El DNI debe ser un número válido"
            return null
        }

        if (noSocioDao.existsNoSocio(dniInt)) {
            textInputLayout?.error = "El DNI ya está registrado"
            return null
        }

        textInputLayout?.error = null
        return dniInt
    }

    private fun validateFields(campos: List<TextInputEditText>): Boolean {
        return campos.map { campo ->
            val textInputLayout = campo.parent.parent as? TextInputLayout
            val esCampoValido = !campo.text.isNullOrBlank()
            textInputLayout?.error = if (esCampoValido) null else "Este campo es obligatorio"
            esCampoValido
        }.all { it }
    }

    private fun clearFields() {
        etNombre.text?.clear()
        etApellido.text?.clear()
        etDni.text?.clear()
        etFechaNacimiento.text?.clear()
        etDomicilio.text?.clear()
        etTelefono.text?.clear()
        etEmail.text?.clear()
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