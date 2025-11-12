package com.aislados.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aislados.clubdeportivo.database.AppDatabase
import com.aislados.clubdeportivo.model.User
import com.aislados.clubdeportivo.model.UserRole
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
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
        val password = findViewById<TextInputEditText>(R.id.password)
        username.requestFocus()

        val database = AppDatabase.getDatabase(context = this)
        val userDao = database.userDao()

        val admin = userDao.findUser("admin")
        if (admin == null) {

            val adminUser = User(
                id = 0,
                username = "admin",
                password = "admin",
                role = UserRole.ADMIN,
                socioId = null
            )
            userDao.createUser(adminUser)
        }


        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val usernameValue = username.text.toString()
            val passwordValue = password.text.toString()
            val user = userDao.findUser(usernameValue, passwordValue)

            if (user != null) {
                Snackbar.make(findViewById(R.id.main), "¡Bienvenido!", Snackbar.LENGTH_SHORT).show()
                login(user)
                finish()
            } else {
                Snackbar.make(findViewById(R.id.main), "Usuario o contraseña incorrectos", Snackbar.LENGTH_LONG)
                    .setAction("Reintentar") {
                        username.text?.clear()
                        password.text?.clear()
                        username.requestFocus()
                    }
                    .show()
            }
        }
    }

    private fun login(user: User) {
        if(user.role == UserRole.ADMIN) {
            Intent(this, MenuPrincipal::class.java)
                .also { startActivity(it) }
        } else {
            Intent(this, CarnetActivity::class.java)
                .apply { putExtra("USER", user) }
                .also { startActivity(it) }
        }
    }
}