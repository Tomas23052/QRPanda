package com.example.QRPanda.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.QRPanda.api.RetrofitClient
import com.example.QRPanda.models.*
import com.example.QRPanda.R
import kotlinx.android.synthetic.main.activity_qrcode_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QRcodeList : AppCompatActivity() {



    lateinit var recyclerview: RecyclerView










    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_list)

        //Inicialização da RecyclerView
        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)

        //Listener do botão localizado na toolbar, para voltar para a atividade anterior
        toolbar.setNavigationOnClickListener {
            finish()
            startActivity(Intent(this@QRcodeList, QRCode::class.java))
        }

        getCodes()




    }




    //Função que vai buscar os códigos QR todos que estão na base de dados para os meter na RecyclerView
    private fun getCodes(){
        RetrofitClient.instance.getQRCode().enqueue(object : Callback<List<QRCodes>>{
            override fun onResponse(call: Call<List<QRCodes>>, response: Response<List<QRCodes>>) {
                if(!response.isSuccessful){
                    d("QRcodeList", response.toString())
                }
                var codeList: List<QRCodes> = response.body()!!
                var codeadapter = QRCodeAdapter(this@QRcodeList, codeList)
                recyclerview.adapter = codeadapter
                codeadapter.onItemClick = {
                    val intent = Intent(this@QRcodeList, QRCodeDetails::class.java)
                    intent.putExtra("code", it)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<QRCodes>>, t: Throwable) {
                Toast.makeText(this@QRcodeList, "Ocorreu um erro", Toast.LENGTH_SHORT).show()
            }

        })
    }

    //Função que é corrida quando o utilizador usa o botão "para trás" ou o movimento swipe back, para acabar com a atividade e começar a anterior
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this@QRcodeList, QRCode::class.java))
    }




}











