package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge // --- ¡AÑADIDO! ---
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat // --- ¡AÑADIDO! ---
import androidx.core.view.WindowInsetsCompat // --- ¡AÑADIDO! ---
import com.aislados.clubdeportivo.database.AppDatabase
import com.aislados.clubdeportivo.database.SocioDAO // --- ¡AÑADIDO! ---
import com.aislados.clubdeportivo.database.UserDAO // --- ¡AÑADIDO! ---
import com.aislados.clubdeportivo.model.Socio
import com.aislados.clubdeportivo.model.User
import com.aislados.clubdeportivo.model.UserRole
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
    private lateinit var userDao: UserDAO
    private lateinit var socioDao: SocioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_alta_socio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = AppDatabase.getDatabase(this)
        userDao = database.userDao()
        socioDao = database.socioDao()


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
        btnRegistrarAlta.setOnClickListener {
            val socio = validateSocioFields()
            val user = validateUserFields()

            if (socio != null && user != null) {
                val intent = Intent(this, CobroActivity::class.java)
                intent.putExtra("USER", user)
                intent.putExtra("SOCIO", socio)
                Toast.makeText(this, "Solicitamos el cobro por inscripción", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateSocioFields(): Socio? {
        val camposTexto = listOf(etNombre, etApellido, etFechaNacimiento, etDomicilio, etTelefono, etEmail)
        val camposValidos = validateFields(camposTexto)
        val dniInt = validateDni()
        val aptoFisicoValido = validateAptoFisico()

        return if (camposValidos && dniInt != null && aptoFisicoValido) {
            Socio(
                nombre = etNombre.text.toString(),
                apellido = etApellido.text.toString(),
                dni = dniInt,
                fechaNacimiento = LocalDate.parse(etFechaNacimiento.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                domicilio = etDomicilio.text.toString(),
                telefono = etTelefono.text.toString(),
                email = etEmail.text.toString(),
                aptoFisico = cbAptoFisico.isChecked
            )
        } else null
    }
    private fun validateUserFields(): User? {
        val camposUsuario = listOf(etUsername, etPassword)
        val camposValidos = validateFields(camposUsuario)
        val username = etUsername.text.toString()
        val userExists = username.isNotBlank() && userDao.existsUser(username)

        if (userExists) {
            (etUsername.parent.parent as? TextInputLayout)?.error = "El nombre de usuario ya existe"
        }

        return if (camposValidos && !userExists) {
            User(username = username, password = etPassword.text.toString(), role = UserRole.SOCIO)
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

        if (socioDao.existsSocio(dniInt)) {
            textInputLayout?.error = "El DNI ya está registrado"
            return null
        }

        textInputLayout?.error = null
        return dniInt
    }
    private fun validateAptoFisico(): Boolean {
        val isValid = cbAptoFisico.isChecked
        cbAptoFisico.error = if (isValid) null else "Debe confirmar el apto físico"
        return isValid
    }
    private fun validateFields(campos: List<TextInputEditText>): Boolean {
        return campos.map { campo ->
            val textInputLayout = campo.parent.parent as? TextInputLayout
            val esCampoValido = !campo.text.isNullOrBlank()
            textInputLayout?.error = if (esCampoValido) null else "Este campo es obligatorio"
            esCampoValido
        }.all { it }
    }
    private fun setupFooter() {
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
            mostrarDialogoDeCierreSesion()
        }
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
    private fun mostrarDialogoDeCierreSesion() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_logout_title)
            .setMessage(R.string.dialog_logout_message)

            // Botón "Sí, Cerrar" (Positivo)
            .setPositiveButton(R.string.dialog_logout_positive) { dialog, which ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            // Botón "Cancelar" (Negativo)
            .setNegativeButton(R.string.dialog_logout_negative) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}