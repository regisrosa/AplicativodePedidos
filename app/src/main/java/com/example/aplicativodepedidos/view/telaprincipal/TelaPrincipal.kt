package com.example.aplicativodepedidos.view.telaprincipal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import com.example.aplicativodepedidos.R
import com.example.aplicativodepedidos.databinding.ActivityTelaPrincipalBinding
import com.example.aplicativodepedidos.view.telalogin.TelaLogin
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.TimeZone

class TelaPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, TelaLogin::class.java)
            startActivity(intent)
            finish()
        }


        binding.btSalvar.setOnClickListener {
            val usuariosMap = hashMapOf(
                "nome" to "Regis", // os dados ainda não estão dinamicos
                "sobrenome" to "de Araújo da Rosa",
                "idade" to "41"
            )

            val pedidoMap = hashMapOf(
                "produto_1" to "cerveja Heinecken",
                "produto_2" to "cerveja Budwiser",
                "produto_3" to "cerveja Paulaner"
            )

            db.collection("Usuários").document("Regis")
                .set(usuariosMap).addOnCompleteListener {
                    Log.d("db", "Usuário salvo com sucesso!")
                }.addOnFailureListener {

                }
        }

/* Este código cria gravações aninhadas. Ex: Clientes -> Ids -> Pedidos -> produtos pedidos

        val userId = FirebaseAuth.getInstance().uid  isso recupera o id de usuario

        db.collection("Usuários").document("Regis").collection("Produtos").document("cervejas")
            .set(pedidoMap).addOnCompleteListener {
                Log.d("db", "Usuário salvo com sucesso!")
            }.addOnFailureListener {

            }

 */


        binding.btLer.setOnClickListener {
            db.collection("Usuários").document("Regis").addSnapshotListener { value, error ->
                if (value != null){
                    val nome = value.getString("nome")
                    val sobrenome = value.getString("sobrenome")
                    val idade = value.getString("idade")
                    binding.tvLerDados.setText("$nome $sobrenome, $idade anos")
                }
            }
        }

        binding.btAtualizar.setOnClickListener {
            db.collection("Usuários").document("Regis")
                .update("nome", "Regis", "sobrenome", "Rosa").addOnCompleteListener {
                    Log.d("update", "Atualizado com sucesso!")
                }
        }

        binding.btDeletar.setOnClickListener {
            db.collection("Usuários").document("Regis")
                .delete().addOnCompleteListener {
                    Log.d("delete", "Documento deletado com sucesso!")
                }
        }
    }
}