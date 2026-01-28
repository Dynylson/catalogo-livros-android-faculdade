package com.example.catalogodelivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etSenha = findViewById<EditText>(R.id.etSenha)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()

            if(email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()

            }else{
                //fazer a logica de autenticação das conta


                //esse bicho aqui direciona para a pagina home
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

                finish()

            }
        }


    }
}