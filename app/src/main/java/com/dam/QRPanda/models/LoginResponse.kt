package com.dam.QRPanda.models

//dados recebidos pela API
data class LoginResponse(val message: String,val error: Boolean, val username: String, val email: String, val id: String, val accessToken : String)
