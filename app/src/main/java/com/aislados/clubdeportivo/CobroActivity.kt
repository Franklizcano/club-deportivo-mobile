package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aislados.clubdeportivo.database.AppDatabase
import com.aislados.clubdeportivo.database.CuotaDAO
import com.aislados.clubdeportivo.database.NoSocioDAO
import com.aislados.clubdeportivo.database.SocioDAO
import com.aislados.clubdeportivo.database.UserDAO
import com.aislados.clubdeportivo.extensions.parcelable
import com.aislados.clubdeportivo.model.Cuota
import com.aislados.clubdeportivo.model.NoSocio
import com.aislados.clubdeportivo.model.Socio
import com.aislados.clubdeportivo.model.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate

class CobroActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var toggleGroup: MaterialButtonToggleGroup
    private lateinit var socioFieldsContainer: LinearLayout
    private lateinit var noSocioFieldsContainer: LinearLayout

    // Campos comunes
    private lateinit var etDniCobro: TextInputEditText
    private lateinit var etNombreCobro: TextInputEditText
    private lateinit var etApellidoCobro: TextInputEditText
    private lateinit var actvMetodoPago: AutoCompleteTextView
    private lateinit var etMonto: TextInputEditText

    // Campos de Socio
    private lateinit var etUltimaCuota: TextInputEditText
    private lateinit var etFechaVencimiento: TextInputEditText

    // Campos de No Socio
    private lateinit var etActividadCobro: TextInputEditText

    private lateinit var tilDniCobro: TextInputLayout

    private var primerPago: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro)

        // Encontrar todos los componentes del layout
        tvTitle = findViewById(R.id.tv_cobro_title)
        toggleGroup = findViewById(R.id.toggle_button_group)
        socioFieldsContainer = findViewById(R.id.socio_fields_container)
        noSocioFieldsContainer = findViewById(R.id.no_socio_fields_container)

        // Inicializar campos comunes
        etDniCobro = findViewById(R.id.et_dni_cobro)
        etNombreCobro = findViewById(R.id.et_nombre_cobro)
        etApellidoCobro = findViewById(R.id.et_apellido_cobro)
        actvMetodoPago = findViewById(R.id.actv_metodo_pago)
        etMonto = findViewById(R.id.et_monto)

        // Inicializar campos de Socio
        etUltimaCuota = findViewById(R.id.et_ultima_cuota)
        etFechaVencimiento = findViewById(R.id.et_fecha_vencimiento)

        // Inicializar campos de No Socio
        etActividadCobro = findViewById(R.id.et_actividad_cobro)

        tilDniCobro = findViewById(R.id.til_dni_cobro)

        // Iniciar con la opción "Socio" seleccionada
        tvTitle.text = getString(R.string.cobro_cuota_socio_title)
        toggleGroup.check(R.id.btn_toggle_socio)

        val database = AppDatabase.getDatabase(this)
        val socioDao: SocioDAO = database.socioDao()
        val noSocioDao: NoSocioDAO = database.noSocioDao()
        val cuotaDao: CuotaDAO = database.cuotaDao()
        val userDao: UserDAO = database.userDao()

        val socio = intent.parcelable<Socio>("SOCIO")
        val user = intent.parcelable<User>("USER")
        val noSocio = intent.parcelable<NoSocio>("NO_SOCIOS")

        socio?.let {
            toggleGroup.check(R.id.btn_toggle_socio)
            etDniCobro.setText(socio.dni.toString())
            etNombreCobro.setText(socio.nombre)
            etApellidoCobro.setText(socio.apellido)
            etDniCobro.isEnabled = false
            toggleGroup.isEnabled = false
            primerPago = true
            tilDniCobro.isEnabled = false
        }

        noSocio?.let {
            toggleGroup.check(R.id.btn_toggle_no_socio)
            tvTitle.text = getString(R.string.cobro_actividad_no_socio_title)
            socioFieldsContainer.visibility = View.GONE
            noSocioFieldsContainer.visibility = View.VISIBLE
            etDniCobro.setText(noSocio.dni.toString())
            etNombreCobro.setText(noSocio.nombre)
            etApellidoCobro.setText(noSocio.apellido)
            etDniCobro.isEnabled = false
            toggleGroup.isEnabled = false
            tilDniCobro.isEnabled = false
            primerPago = true
        }

        // --- LÓGICA DEL TOGGLE (EL INTERRUPTOR) ---
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            if (checkedId == R.id.btn_toggle_socio) {
                tvTitle.text = getString(R.string.cobro_cuota_socio_title)
                socioFieldsContainer.visibility = View.VISIBLE
                noSocioFieldsContainer.visibility = View.GONE
            } else {
                tvTitle.text = getString(R.string.cobro_actividad_no_socio_title)
                socioFieldsContainer.visibility = View.GONE
                noSocioFieldsContainer.visibility = View.VISIBLE
            }
        }

        // --- LÓGICA PARA LOS MENÚS DESPLEGABLES ---

        val metodosDePago = listOf("Efectivo", "3 Cuotas", "6 Cuotas")

        val adapterMetodos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, metodosDePago)
        actvMetodoPago.setAdapter(adapterMetodos)

        // --- LÓGICA DE BÚSQUEDA ---
        tilDniCobro.setEndIconOnClickListener {
            Toast.makeText(this, "Buscando DNI...", Toast.LENGTH_SHORT).show()
            if (etDniCobro.text.toString().isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese un DNI", Toast.LENGTH_SHORT).show()
            } else {
                val socio = socioDao.findSocioByDni(etDniCobro.text.toString().toInt())
                socio.let {
                    etNombreCobro.setText(socio?.nombre)
                    etApellidoCobro.setText(socio?.apellido)
                    etDniCobro.isEnabled = false
                }
                val ultimaCuotaPaga = socio?.id?.let { cuotaDao.findCuotaBySocioIdOrderByIdDesc(socio.id) }
                ultimaCuotaPaga?.let {
                    etUltimaCuota.setText(it.fechaPago.toString())
                    etFechaVencimiento.setText(it.fechaVencimiento.toString())
                }
            }
        }

        // --- LÓGICA DEL BOTÓN REGISTRAR PAGO ---
        findViewById<MaterialButton>(R.id.btn_registrar_pago).setOnClickListener {
            if (validateFields()) {
                if (toggleGroup.checkedButtonId == R.id.btn_toggle_socio) {
                    if (primerPago) {
                        val socioId = socioDao.createSocio(socio!!)
                        userDao.createUser(user!!.copy(socioId = socioId))
                        cuotaDao.createCuota(buildCuota(socioId, lastExpirationDate = LocalDate.now()))
                        val intent = Intent(this, MenuPrincipal::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    } else {
                        val socioId = socioDao.findSocioByDni(etDniCobro.text.toString().toInt())!!.id
                        val ultimaCuotaPaga = cuotaDao.findCuotaBySocioIdOrderByIdDesc(socioId)
                        cuotaDao.createCuota(buildCuota(socioId = socioId, lastExpirationDate = ultimaCuotaPaga!!.fechaVencimiento))
                    }
                } else {
                    if (primerPago) {
                        val noSocioId = noSocioDao.createNoSocio(noSocio!!)
                        cuotaDao.createCuota(buildCuota(noSocioId = noSocioId, lastExpirationDate = null))
                        val intent = Intent(this, MenuPrincipal::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    } else {
                        val noSocioId = noSocioDao.findNoSocioByDni(etDniCobro.text.toString().toInt())!!.id
                        cuotaDao.createCuota(buildCuota(noSocioId = noSocioId, lastExpirationDate = null))
                    }
                }
                Toast.makeText(this, "Pago registrado exitosamente", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        setupFooter()
    }

    private fun buildCuota(socioId: Long? = null, noSocioId: Long? = null, lastExpirationDate: LocalDate?): Cuota {
        val fechaVencimiento = lastExpirationDate?.plusMonths(1)

        val cuota = Cuota(
            socioId = socioId,
            noSocioId = noSocioId,
            fechaPago = LocalDate.now(),
            fechaVencimiento = fechaVencimiento ?: LocalDate.now(),
            monto = etMonto.text.toString().toDouble(),
            metodoPago = actvMetodoPago.text.toString(),
            actividad = if (toggleGroup.checkedButtonId == R.id.btn_toggle_socio) null else etActividadCobro.text.toString()
        )
        return cuota
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

    private fun validateFields(): Boolean {
        val camposComunesValidos = validateCommonFields()

        return if (toggleGroup.checkedButtonId == R.id.btn_toggle_socio) {
            camposComunesValidos && validateSocioFields()
        } else {
            camposComunesValidos && validateNoSocioFields()
        }
    }

    private fun validateCommonFields(): Boolean {
        val camposDeTexto = listOf(
            etDniCobro, etNombreCobro, etApellidoCobro, etMonto
        )

        val textFieldsValid = camposDeTexto.all { campo ->
            val textInputLayout = campo.parent.parent as? TextInputLayout
            val esCampoValido = !campo.text.isNullOrBlank()

            textInputLayout?.error = if (esCampoValido) null else "Este campo es obligatorio"

            esCampoValido
        }

        val dniInt = etDniCobro.text.toString().toIntOrNull()
        val tilDni = etDniCobro.parent.parent as? TextInputLayout
        val dniValido = dniInt != null && !etDniCobro.text.isNullOrBlank()
        tilDni?.error = if (dniValido) null else "El DNI debe ser un número válido"

        val tilMetodoPago = actvMetodoPago.parent.parent as? TextInputLayout
        val metodoPagoValido = !actvMetodoPago.text.isNullOrBlank()
        tilMetodoPago?.error = if (metodoPagoValido) null else "Seleccione un método de pago"

        val montoDouble = etMonto.text.toString().toDoubleOrNull()
        val tilMonto = etMonto.parent.parent as? TextInputLayout
        val montoValido = montoDouble != null && montoDouble > 0
        tilMonto?.error = if (montoValido) null else "El monto debe ser mayor a 0"

        return textFieldsValid && dniValido && metodoPagoValido && montoValido
    }

    private fun validateSocioFields(): Boolean {
        if (primerPago) {
            return true
        }

        val camposSocio = listOf(etUltimaCuota, etFechaVencimiento)

        return camposSocio.all { campo ->
            val textInputLayout = campo.parent.parent as? TextInputLayout
            val esCampoValido = !campo.text.isNullOrBlank()

            textInputLayout?.error = if (esCampoValido) null else "Este campo es obligatorio"

            esCampoValido
        }
    }

    private fun validateNoSocioFields(): Boolean {
        val textInputLayout = etActividadCobro.parent.parent as? TextInputLayout
        val esCampoValido = !etActividadCobro.text.isNullOrBlank()

        textInputLayout?.error = if (esCampoValido) null else "Este campo es obligatorio"

        return esCampoValido
    }

    private fun clearFields() {
        etDniCobro.text?.clear()
        etDniCobro.isEnabled = true
        etNombreCobro.text?.clear()
        etApellidoCobro.text?.clear()
        etMonto.text?.clear()
        actvMetodoPago.text?.clear()
        etUltimaCuota.text?.clear()
        etFechaVencimiento.text?.clear()
        etActividadCobro.text?.clear()

        (etDniCobro.parent.parent as? TextInputLayout)?.error = null
        (etNombreCobro.parent.parent as? TextInputLayout)?.error = null
        (etApellidoCobro.parent.parent as? TextInputLayout)?.error = null
        (etMonto.parent.parent as? TextInputLayout)?.error = null
        (actvMetodoPago.parent.parent as? TextInputLayout)?.error = null
        (etUltimaCuota.parent.parent as? TextInputLayout)?.error = null
        (etFechaVencimiento.parent.parent as? TextInputLayout)?.error = null
        (etActividadCobro.parent.parent as? TextInputLayout)?.error = null
    }
}