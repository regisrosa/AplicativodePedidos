package com.example.aplicativodepedidos.view.telalogin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.aplicativodepedidos.databinding.ActivityTelaLoginBinding
import com.example.aplicativodepedidos.view.telacadastro.TelaCadastro
import com.example.aplicativodepedidos.view.telaprincipal.TelaPrincipal
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class TelaLogin : AppCompatActivity() {

    private lateinit var binding: ActivityTelaLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btEntrar.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val senha = binding.etSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Snackbar.make(it, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.RED).show()
            } else {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { autenticacao ->
                        if (autenticacao.isSuccessful) {
                            val intent = Intent(this, TelaPrincipal::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }.addOnFailureListener {exception ->
                        val mensagemErro = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                            is FirebaseNetworkException -> "Sem internet!"
                            else -> "Erro ao cadastrar usuário!"
                        }
                        Snackbar.make(it, mensagemErro, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.RED).show()
                    }


            }

        }

        binding.textoIrTelaCadastro.setOnClickListener {
            val intent = Intent(this, TelaCadastro::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            val intent = Intent(this, TelaPrincipal::class.java)
            startActivity(intent)
            finish()
        }
    }


}
