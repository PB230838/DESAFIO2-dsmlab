package com.example.desafio2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var bdd: BDD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        bdd = BDD(this)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister() {
        val username = findViewById<EditText>(R.id.etUsername).text.toString().trim()
        val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()

        if (username.isEmpty()) {
            showError(R.id.etUsername, "Complete el campo de usuario")
        } else if (password.isEmpty()) {
            showError(R.id.etPassword, "Complete el campo de contrasenia")
        } else {
            val result = bdd.addUser(username, password)
            if (result > 0) {
                showToast("Te has registrado correctamente")
                finish()
            }
        }
    }

    private fun showError(viewId: Int, message: String) {
        findViewById<EditText>(viewId).error = message
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
