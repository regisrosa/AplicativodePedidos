package com.example.aplicativodepedidos.view.telacadastro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplicativodepedidos.databinding.ActivityTelaCadastroBinding
import com.example.aplicativodepedidos.view.telacompletarcadastro.TelaCompletarCadastro
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class TelaCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityTelaCadastroBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btCadastrar.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val senha = binding.etSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {

                Snackbar.make(it, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.RED).show()

            } else {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            Snackbar.make(
                                it,
                                "Usuário cadastrado com sucesso!",
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(Color.GREEN).show()

                            binding.etEmail.setText("")
                            binding.etSenha.setText("")

                            Thread.sleep(3000)

                            val intent = Intent(this, TelaCompletarCadastro::class.java)
                            startActivity(intent)
                            finish()

                        }
                    }.addOnFailureListener { exception ->

                        val mensagemErro = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha de no mínimo 6 caracteres!"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                            is FirebaseAuthUserCollisionException -> "Esta conta já existe!"
                            is FirebaseNetworkException -> "Sem internet!"
                            else -> "Erro ao cadastrar usuário!"
                        }
                        Snackbar.make(it, mensagemErro, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.RED).show()


                    }

            }

        }

    }

}