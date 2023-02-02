package com.example.QRPanda.activities


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.QRPanda.api.RetrofitClient
import com.example.QRPanda.models.QRCodes
import com.example.QRPanda.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_qrcode_details.*
import kotlinx.android.synthetic.main.activity_qrcode_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Atividade que mostra os detalhes de um item clicado no histórico e o utilizador tem a escolha de atualizar o código QR ou eliminá-lo se assim o desejar
class QRCodeDetails : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_details)



        //Valor passado da atividade da Lista para o código ser gerado ao começar a atividade
        val code = intent.getParcelableExtra<QRCodes>("code")
        if(code != null){
            et_qrcode.setText(code.messageQR)
            initUpdateButton(code._id)
            iniDeleteButton(code._id)
        }

        //Listener para o botão localizado na toolbar para ir para a atividade anterior
        toolbar_details.setNavigationOnClickListener {
            finish()
            startActivity(Intent(this@QRCodeDetails, QRcodeList::class.java))
        }


        init()

    }

    //Função que é corrida quando o botão Atualizar é pressionado
    private fun initUpdateButton(id: String){
        btn_update.setOnClickListener {
            val data = et_qrcode.text.toString().trim()

            //Se o campo et_qrcode estiver vazio o utilizador vai ser avisado, se não o código é atualizado consoante o que estiver nesse campo
            if (data.isEmpty()) {
                et_qrcode.error = "É necessário inserir dados"
                et_qrcode.requestFocus()
                return@setOnClickListener
            } else {
                val writer = QRCodeWriter()
                try {
                    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    iVQRcode.setImageBitmap(bmp)
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
            }
            val message = et_qrcode.text.toString().trim()

            //PATCH, reposta do retrofit para atualizar o código QR
            RetrofitClient.instance.updateQRCode(id, message).enqueue(object : Callback<QRCodes>{
                override fun onResponse(call: Call<QRCodes>, response: Response<QRCodes>) {

                    d("resposta", response.body().toString())
                    Toast.makeText(this@QRCodeDetails, "Código QR atualizado com sucesso", Toast.LENGTH_SHORT).show()
                    d("Atualizado", response.toString())


                }

                override fun onFailure(call: Call<QRCodes>, t: Throwable) {
                    d("Erro", t.toString())
                }

            })
        }
    }

    //Função que é corrida quando o botão Eliminar é pressionado
    private fun iniDeleteButton(id: String){
        btn_delete.setOnClickListener {
            //DELETE, resposta do retrofit para eliminar o código QR
            RetrofitClient.instance.deleteQRCode(id).enqueue(object : Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    finish()
                    startActivity(Intent(this@QRCodeDetails, QRcodeList::class.java))
                    Toast.makeText(this@QRCodeDetails, "Código QR apagado com sucesso", Toast.LENGTH_SHORT).show()


                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(this@QRCodeDetails, "Não foi possível apagar o código QR", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    //Função inicial para desenhar o código QR da mensagem clicada no histórico
    private fun init(){
        val data = et_qrcode.text.toString().trim()


            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                iVQRcode.setImageBitmap(bmp)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }


    //Função que é corrida quando o utilizador usa o botão "para trás" ou o movimento swipe back, para acabar com a atividade e começar a anterior
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this@QRCodeDetails, QRcodeList::class.java))
    }


}