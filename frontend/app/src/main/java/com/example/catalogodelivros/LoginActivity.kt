package com.example.catalogodelivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.catalogodelivros.api.LoginRequest
import com.example.catalogodelivros.api.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etSenha = findViewById<EditText>(R.id.etSenha)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.isEnabled = false
            performLogin(email, senha, btnLogin)
        }
    }

    private fun performLogin(email: String, password: String, btnLogin: Button) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        sessionManager.saveAuthData(
                            token = loginResponse.token,
                            userId = loginResponse.user.id,
                            email = loginResponse.user.email,
                            name = loginResponse.user.name
                        )

                        Toast.makeText(
                            this@LoginActivity,
                            "Bem-vindo, ${loginResponse.user.name}!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Email ou senha incorretos"
                        else -> "Erro ao fazer login"
                    }
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    btnLogin.isEnabled = true
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Erro de conexão: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                btnLogin.isEnabled = true
            }
        }
    }
}