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
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CobroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro)

        // Encontrar todos los componentes del layout
        val tvTitle = findViewById<TextView>(R.id.tv_cobro_title)
        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggle_button_group)
        val btnToggleSocio = findViewById<MaterialButton>(R.id.btn_toggle_socio)
        val socioFieldsContainer = findViewById<LinearLayout>(R.id.socio_fields_container)
        val noSocioFieldsContainer = findViewById<LinearLayout>(R.id.no_socio_fields_container)

        // Iniciar con la opción "Socio" seleccionada
        tvTitle.text = getString(R.string.cobro_cuota_socio_title)
        toggleGroup.check(R.id.btn_toggle_socio)

        // --- LÓGICA DEL TOGGLE (EL INTERRUPTOR) ---
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            if (checkedId == R.id.btn_toggle_socio) {
                // Mostrar campos de Socio, ocultar los de No Socio
                tvTitle.text = getString(R.string.cobro_cuota_socio_title)
                socioFieldsContainer.visibility = View.VISIBLE
                noSocioFieldsContainer.visibility = View.GONE
            } else {
                // Mostrar campos de No Socio, ocultar los de Socio
                tvTitle.text = getString(R.string.cobro_actividad_no_socio_title)
                socioFieldsContainer.visibility = View.GONE
                noSocioFieldsContainer.visibility = View.VISIBLE
            }
        }

        // --- LÓGICA PARA LOS MENÚS DESPLEGABLES ---

        val metodosDePago = listOf("Efectivo", "3 Cuotas", "6 Cuotas")

        val adapterMetodos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, metodosDePago)
        findViewById<AutoCompleteTextView>(R.id.actv_metodo_pago).setAdapter(adapterMetodos)

        // --- LÓGICA PARA LOS DATE PICKERS ---
        val etUltimaCuota = findViewById<TextInputEditText>(R.id.et_ultima_cuota)
        val etFechaVencimiento = findViewById<TextInputEditText>(R.id.et_fecha_vencimiento)

        setupDatePicker(etUltimaCuota)
        setupDatePicker(etFechaVencimiento)

        // --- LÓGICA DE BÚSQUEDA ---
        findViewById<TextInputLayout>(R.id.til_dni_cobro).setEndIconOnClickListener {
            Toast.makeText(this, "Buscando DNI...", Toast.LENGTH_SHORT).show()
            // Aquí iría la llamada a la base de datos
            findViewById<TextInputEditText>(R.id.et_nombre_cobro).setText("Juan")
            findViewById<TextInputEditText>(R.id.et_apellido_cobro).setText("Perez")
        }

        // --- LÓGICA DEL BOTÓN REGISTRAR PAGO ---
        findViewById<MaterialButton>(R.id.btn_registrar_pago).setOnClickListener {
            Toast.makeText(this, "Registrando pago...", Toast.LENGTH_SHORT).show()
        }

        // --- LÓGICA DEL FOOTER (reutilizada) ---
        setupFooter()
    }

    private fun setupDatePicker(editText: TextInputEditText) {
        editText.isFocusable = false
        editText.isClickable = true
        editText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()

            datePicker.addOnPositiveButtonClickListener { utcMillis ->
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(utcMillis)
                val localDate = Date(utcMillis + offset)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("es", "AR"))
                editText.setText(sdf.format(localDate))
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_${editText.id}")
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
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}