package com.example.QRPanda.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.QRPanda.R

import kotlinx.android.synthetic.main.activity_about_us.*

//Atividade que contém algumas informações sobre a aplicação e os alunos que a desenvolveram
class AboutUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        toolbar_about.setNavigationOnClickListener {
            finish()
            startActivity(Intent(this@AboutUs, QRCode::class.java))
        }
    }


    //Função que é corrida quando o utilizador usa o botão "para trás" ou o movimento swipe back, para acabar com a atividade e começar a anterior
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this@AboutUs, QRCode::class.java))
    }
}