package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aislados.clubdeportivo.database.UserDAO
import com.aislados.clubdeportivo.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlin.text.clear

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = findViewById<TextInputEditText>(R.id.username)
        username.requestFocus()

        val userTable = UserDAO(this)
        val userTest = User("admin", "admin")
        userTable.createUser(userTest)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val password = findViewById<TextInputEditText>(R.id.password)

            if (userTable.existsUser(username.text.toString(), password.text.toString())) {
                Snackbar.make(findViewById(R.id.main), "¡Bienvenido!", Snackbar.LENGTH_SHORT).show()
                Intent(this, MenuPrincipal::class.java)
                    .also { startActivity(it) }
            } else {
                Snackbar.make(findViewById(R.id.main), "Usuario o contraseña incorrectos", Snackbar.LENGTH_LONG)
                    .setAction("Reintentar") {
                        // Limpiar campos
                        username.text?.clear()
                        password.text?.clear()
                        username.requestFocus()
                    }
                    .show()
            }
        }
    }
}