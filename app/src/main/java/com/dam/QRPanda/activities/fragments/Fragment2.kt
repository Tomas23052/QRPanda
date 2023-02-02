package com.dam.QRPanda.activities.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import me.dm7.barcodescanner.zxing.ZXingScannerView



//Fragmento onde se poderá fazer uma leitura de códigos QR
class Fragment2 : Fragment(), ZXingScannerView.ResultHandler{


    lateinit var scannerView: ZXingScannerView

    lateinit var message: String







    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        //Define a view do fragmento como a variável scannerView como um ZXingScannerView que é a biblioteca usada para ler os códigos QR
        scannerView = ZXingScannerView(activity)
        //Pedido de permissão para usar a camera caso o utilizador ainda não tenha dado usando a biblioteca "com.karumi.dexter"
        Dexter.withContext(this.requireContext())
            .withPermission(android.Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    scannerView.startCamera()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    TODO("Not yet implemented")
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()

        //retorna a variável para que seja apresentada na aplicação
        return  scannerView












    }


    //Função que vai tratar do resultado da leitura
    override fun handleResult(rawResult: Result?) {
        message = rawResult?.text.toString()
        //Se a mensagem lida conter algum tipo dessas condições, um AlertDialog é aberto a apresentar a mensagem lida que neste caso seria um link e o utilizador tem a escolha de abrir o link ou cancelar e continuar a leitura
        if(message.contains("https://") || message.contains("http://") || message.contains("www.")){
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Código QR")
            builder.setMessage(message)
            builder.setNegativeButton("Cancelar") {_, _ ->  onResume()}
            builder.setPositiveButton("Abrir") { _, _ -> goWeb() }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }else {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Código QR")
            builder.setMessage(message)
            builder.setPositiveButton("OK") { _, _ -> onResume() }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }


    //Função que é corrida quando o fragmento volta a abrir para que a camera seja utilizada outra vez e definir o Handler do resultado da leitura
    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    //Função que é corrida quando o fragmento não está em utilização que apenas pára a camera
    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }


    //Função utilizada para abrir um browser para abrir um link caso a leitura seja um link
    fun goWeb(){
        var i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(message)
        startActivity(i)
    }




}