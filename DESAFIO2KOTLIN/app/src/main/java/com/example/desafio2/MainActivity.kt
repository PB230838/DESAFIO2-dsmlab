package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: BDD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = BDD(this)


        initializeDatabase()

        setupUI()
    }

    private fun setupUI() {
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
            handleLogin(etUsername, etPassword)
        }

        btnRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun handleLogin(usernameField: EditText, passwordField: EditText) {
        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Usuario y contraseña son obligatorios")
            return
        }

        if (dbHelper.checkUser(username, password)) {
            showToast("Inicio de sesión exitoso")
            startActivity(Intent(this, MenuActivity::class.java))
        } else {
            showToast("Datos incorrectos")
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun initializeDatabase() {
        val db = dbHelper.writableDatabase
        db.delete(BDD.TABLE_MENU, null, null)

        val spicyMexicanMenuItems = listOf(
            "Tacos de Carnitas Picantes" to Pair(9.99, "food"),
            "Elote con Salsa de Habanero" to Pair(6.99, "food"),
            "Chiles en Nogada Picantes" to Pair(12.99, "food"),
            "Sopes con Salsa de Chile Seco" to Pair(7.99, "food"),
            "Tostadas de Ceviche con Salsa Roja" to Pair(8.99, "food"),
            "Tacos de Lengua con Salsa Verde" to Pair(10.99, "food"),
            "Tacos de Tripas con Salsa de Chile" to Pair(11.99, "food"),
            "Michelada con Salsa Picante" to Pair(4.99, "drink"),
            "Tequila con Infusión de Chile" to Pair(5.99, "drink"),
            "Mezcal con Gusano y Chile" to Pair(6.99, "drink")
        )

        spicyMexicanMenuItems.forEach { (name, details) ->
            val (price, type) = details
            dbHelper.insertMenuItem(name, price, type)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
