package com.example.QRPanda.models

//Dados recebidos da API
data class QRCodeResponse (val message: String, val messageQR: String, val error: Boolean)
