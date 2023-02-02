package com.dam.QRPanda.activities.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.dam.QRPanda.activities.AboutUs
import com.dam.QRPanda.activities.LoginActivity
import com.dam.QRPanda.activities.QRcodeList
import com.dam.QRPanda.api.RetrofitClient
import com.dam.QRPanda.models.QRCodeResponse
import com.dam.QRPanda.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.fragment_1.*
import kotlinx.android.synthetic.main.fragment_1.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//Fragmento onde se pode gerar códigos QR, dar logout, abrir a atividade "Sobre nós" e o histórico dos códigos gerados
class Fragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_1, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        //Listener para o botão do histórico que começa a atividade do mesmo
        view.btnHistory.setOnClickListener {

            startActivity(Intent(activity,QRcodeList::class.java ))
        }

        //Listener para de logout que abre um AlertDialog de confirmação para sair ou continuar na aplicação
        view.btn_logout.setOnClickListener {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Deseja sair da aplicação?")
            builder.setNegativeButton("Não") {_, _ ->  onResume()}
            builder.setPositiveButton("Sim") { _, _ -> start() }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        //Listener para o botão que vai abrir a atividade onde tem as informações sobre os alunos desenvolvedores
        view.btn_about.setOnClickListener {
            activity?.finish()
            startActivity(Intent(activity, AboutUs::class.java))
        }

        //Listener que vai gerar o código QR consoante o que está no campo de texto etData
        view.btnGen.setOnClickListener {



            val data = etData.text.toString().trim()


            //Se o campo de texto estiver vazio o utilizador irá receber um alerta, se não o código vai ser generado usando a biblioteca "com.google.zxing.qrcode"
            if (data.isEmpty()) {
                etData.error = "É necessário inserir dados"
                etData.requestFocus()
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
                    ivQRcode.setImageBitmap(bmp)
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
                //Em seguida manda a mensagem usada para criar o código para a base de dados através da API
                RetrofitClient.instance.fetchCodes( messageQR = view.etData.text.toString().trim()).enqueue(object : Callback<QRCodeResponse>{
                    override fun onResponse(
                        call: Call<QRCodeResponse>,
                        response: Response<QRCodeResponse>
                    ) {
                        Toast.makeText(
                            view.context,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                        etData.text= null
                    }

                    override fun onFailure(call: Call<QRCodeResponse>, t: Throwable) {
                        Toast.makeText(view.context, t.message, Toast.LENGTH_LONG).show()
                    }

                })
            }


        }

        return view
    }


    //Função para começar a atividade Login depois de confirmar a saída no AlertDialog
    private fun start(){
        activity?.finish()
        startActivity(Intent(activity, LoginActivity::class.java))
    }



}