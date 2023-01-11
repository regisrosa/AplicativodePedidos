package com.example.aplicativodepedidos.view.telaprincipal

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.icu.util.LocaleData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.aplicativodepedidos.R
import com.example.aplicativodepedidos.databinding.ActivityTelaPrincipalBinding
import com.example.aplicativodepedidos.view.tela_atualizar.TelaAtualizar
import com.example.aplicativodepedidos.view.telalogin.TelaLogin
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class TelaPrincipal : AppCompatActivity() {

    //----------variaveis globais----------
    private lateinit var binding: ActivityTelaPrincipalBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var qtd1 = 0
    var qtd2 = 0
    var qtd3 = 0
    var qtd4 = 0

    //-------------------------------------


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        val usuarioCorrente = auth.currentUser?.email.toString()
        db.collection("Clientes").document(usuarioCorrente).addSnapshotListener { value, error ->
            if (value != null) {
                val nomeCompleto = value.getString("nomeCompleto")
                binding.tvNomeCabecalho.setText(nomeCompleto)
            }
        }


        binding.btDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, TelaLogin::class.java)
            startActivity(intent)
            finish()

        }


        //mudarCorContainer()

        escolherQuantidade()

        irTelaAtualizar()


    }

/*
    private fun mudarCorContainer(){
        binding.btMais1.setOnClickListener {
            if (qtd1 > 0){
                binding.rl3.background.setTint(Color.WHITE)
            }
        }
        binding.btMenos1.setOnClickListener {
            if (qtd1 == 0){
                binding.rl3.background.setTint(Color.LTGRAY)
            }
        }


        if (qtd2 > 0){
            binding.rl4.background.setTint(Color.WHITE)
        }else{
            binding.rl4.background.setTint(Color.LTGRAY)
        }

        if (qtd3 > 0){
            binding.rl5.background.setTint(Color.WHITE)
        }else{
            binding.rl5.background.setTint(Color.LTGRAY)
        }

        if (qtd4 > 0){
            binding.rl6.background.setTint(Color.WHITE)
        }else{
            binding.rl6.background.setTint(Color.LTGRAY)
        }



    }

 */


    private fun escolherQuantidade() {

        binding.btMais1.setOnClickListener {
            qtd1++
            binding.tvQuantidade1.setText(qtd1.toString())
            binding.rl3.background.setTint(Color.WHITE)
        }
        binding.btMenos1.setOnClickListener {
            if (qtd1 > 0) {
                qtd1--
                binding.tvQuantidade1.setText(qtd1.toString())
            }
            if(qtd1 == 0){
                binding.rl3.background.setTint(Color.rgb(225, 225,225))
            }
        }


        binding.btMais2.setOnClickListener {
            qtd2++
            binding.tvQuantidade2.setText(qtd2.toString())
            binding.rl4.background.setTint(Color.WHITE)
        }
        binding.btMenos2.setOnClickListener {
            if (qtd2 > 0) {
                qtd2--
                binding.tvQuantidade2.setText(qtd2.toString())
            }
            if (qtd2 == 0){
                binding.rl4.background.setTint(Color.rgb(225, 225, 225))
            }
        }


        binding.btMais3.setOnClickListener {
            qtd3++
            binding.tvQuantidade3.setText(qtd3.toString())
            binding.rl5.background.setTint(Color.WHITE)
        }
        binding.btMenos3.setOnClickListener {
            if (qtd3 > 0) {
                qtd3--
                binding.tvQuantidade3.setText(qtd3.toString())
            }
            if (qtd3 == 0){
                binding.rl5.background.setTint(Color.rgb(225, 225, 225))
            }
        }


        binding.btMais4.setOnClickListener {
            qtd4++
            binding.tvQuantidade4.setText(qtd4.toString())
            binding.rl6.background.setTint(Color.WHITE)
        }
        binding.btMenos4.setOnClickListener {
            if (qtd4 > 0) {
                qtd4--
                binding.tvQuantidade4.setText(qtd4.toString())
            }
            if (qtd4 == 0){
                binding.rl6.background.setTint(Color.rgb(225, 225, 225))
            }
        }

    }

    private fun irTelaAtualizar() {

        binding.tvAtualizar.setOnClickListener {
            val intent = Intent(this, TelaAtualizar::class.java)
            startActivity(intent)
            finish()
        }
    }


    public fun fazerPedido(view: View) {
        var pedido = hashMapOf<String, String>()

        if (qtd1 > 0) {
            pedido.put("produto1", "Budwiser Qtd: $qtd1")
        }

        if (qtd2 > 0) {
            pedido.put("produto2", "Heinecken Qtd: $qtd2")
        }

        if (qtd3 > 0) {
            pedido.put("produto3", "Amstel Qtd: $qtd3")
        }

        if (qtd4 > 0) {
            pedido.put("produto4", "Petra Qtd: $qtd4")
        }
        

        val usuarioCorrente = auth.currentUser?.email.toString()
        val data = LocalDate.now().toString()
        val hora = LocalTime.now().toString()

        val formatoOriginal = SimpleDateFormat("yyyy-MM-dd").parse(data)
        val formatoBrasileiro = SimpleDateFormat("dd-MM-yyyy").format(formatoOriginal)

        val dataPedido = "$formatoBrasileiro $hora"

        val sb = StringBuilder()
        for (item in pedido){
            sb.append(item.value + "\n")
        }
        AlertDialog.Builder(this)
            .setTitle("Pedido $formatoBrasileiro")
            .setMessage("Confirma seu pedido?\n$sb")
            .setPositiveButton("sim", DialogInterface.OnClickListener { dialogInterface, i ->

                db.collection("Clientes").document(usuarioCorrente)
                    .collection("Pedidos").document(dataPedido).set(pedido).addOnCompleteListener { pedido ->
                        if (pedido.isSuccessful){
                            Toast.makeText(this, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show()
                        }
                    }

            })
            .setNegativeButton("não", null)
            .create().show()

    }


}