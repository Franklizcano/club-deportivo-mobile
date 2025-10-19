package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aislados.clubdeportivo.database.SocioDAO
import com.aislados.clubdeportivo.database.UserDAO
import com.aislados.clubdeportivo.model.Socio
import com.aislados.clubdeportivo.model.User
import com.aislados.clubdeportivo.model.UserRole
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AltaSocioActivity : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var etDomicilio: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnRegistrarAlta: MaterialButton
    private lateinit var cbAptoFisico: MaterialCheckBox
    val userTable = UserDAO(this)
    val socioTable = SocioDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_socio)

        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        etDni = findViewById(R.id.et_dni)
        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento)
        etDomicilio = findViewById(R.id.et_domicilio)
        etTelefono = findViewById(R.id.et_telefono)
        etEmail = findViewById(R.id.et_email)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnRegistrarAlta = findViewById(R.id.btn_registrar_alta)
        cbAptoFisico = findViewById(R.id.cb_apto_fisico)

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

        btnAtras.setOnClickListener {
            finish()
        }

        btnMenuPrincipal.setOnClickListener {
            val intent = Intent(this, MenuPrincipal::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnRegistrarAlta.setOnClickListener {
            val socio = validateSocioFields()
            val user = validateUserFields()

            if (socio != null && user != null) {
                socioTable.createSocio(socio)
                userTable.createUser(user)
                clearFields()
                Toast.makeText(this, "Alta exitosa", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateSocioFields(): Socio? {
        val camposTextoValidos = validateTextFields()
        val dniInt = validateDni()
        val aptoFisicoValido = validateAptoFisico()

        if (camposTextoValidos && dniInt != null && aptoFisicoValido) {
            val fechaLocalDate = parseFecha()

            return Socio(
                nombre = etNombre.text.toString(),
                apellido = etApellido.text.toString(),
                dni = dniInt,
                fechaNacimiento = fechaLocalDate,
                domicilio = etDomicilio.text.toString(),
                telefono = etTelefono.text.toString(),
                email = etEmail.text.toString(),
                aptoFisico = cbAptoFisico.isChecked
            )
        }

        return null
    }

    private fun validateUserFields(): User? {
        val camposUsuario = listOf(etUsername, etPassword)

        val isValid = camposUsuario.all { campo ->
            val textInputLayout = campo.parent.parent as? TextInputLayout
            val esCampoValido = !campo.text.isNullOrBlank()

            textInputLayout?.error = if (esCampoValido) null else "Este campo es obligatorio"

            esCampoValido
        }

        val userExists = userTable.existsUser(etUsername.text.toString())

        if (userExists) {
            val textInputLayout = etUsername.parent.parent as? TextInputLayout
            textInputLayout?.error = "El nombre de usuario ya existe"
        }

        return if (isValid && !userExists) {
            User(
                username = etUsername.text.toString(),
                password = etPassword.text.toString(),
                role = UserRole.SOCIO
            )
        } else {
            null
        }
    }

    private fun validateTextFields(): Boolean {
        val camposDeTexto = listOf(
            etNombre, etApellido, etDni, etFechaNacimiento,
            etDomicilio, etTelefono, etEmail
        )

        return camposDeTexto.all { campo ->
            val textInputLayout = campo.parent.parent as? TextInputLayout
            val esCampoValido = !campo.text.isNullOrBlank()

            if (esCampoValido) {
                textInputLayout?.error = null
            } else {
                textInputLayout?.error = "Este campo es obligatorio"
            }

            esCampoValido
        }
    }

    private fun validateDni(): Int? {
        val dniInt = etDni.text.toString().toIntOrNull()
        val textInputLayout = etDni.parent.parent as? TextInputLayout
        val socioTable = SocioDAO(this)

        return if (dniInt == null || etDni.text.isNullOrBlank() || socioTable.existsSocio(dniInt)) {
            textInputLayout?.error = "El DNI debe ser un número válido"
            null
        } else {
            textInputLayout?.error = null
            dniInt
        }
    }

    private fun validateAptoFisico(): Boolean {
        return if (cbAptoFisico.isChecked) {
            cbAptoFisico.error = null
            true
        } else {
            cbAptoFisico.error = "Debe confirmar el apto físico"
            false
        }
    }

    private fun parseFecha(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(etFechaNacimiento.text.toString(), formatter)
    }

    private fun clearFields() {
        etNombre.text?.clear()
        etApellido.text?.clear()
        etDni.text?.clear()
        etFechaNacimiento.text?.clear()
        etDomicilio.text?.clear()
        etTelefono.text?.clear()
        etEmail.text?.clear()
        etUsername.text?.clear()
        etPassword.text?.clear()
        cbAptoFisico.isChecked = false
    }
}