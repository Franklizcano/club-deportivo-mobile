package com.aislados.clubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aislados.clubdeportivo.database.UserDAO
import com.aislados.clubdeportivo.model.User
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userTable = UserDAO(this)
        val userTest = User("admin", "admin")
        userTable.createUser(userTest)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val username = findViewById<TextInputEditText>(R.id.username).text.toString()
            val password = findViewById<TextInputEditText>(R.id.password).text.toString()
            if (userTable.existsUser(username, password)) {
                Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}