package com.dam.QRPanda.api

import com.dam.QRPanda.models.*
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface interfaceapi {

    //Vai buscar uma lista de códigos QR à base de dados
    @GET("qrcode/all")
    fun getQRCode(): retrofit2.Call<List<QRCodes>>

    //Atualiza um código QR consoante o seu ID
    @FormUrlEncoded
    @PATCH("qrcode/change/{id}")
    fun updateQRCode(@Path("id") id: String, @Field("messageQR") messageQR: String): retrofit2.Call<QRCodes>


    //Mete os parâmetros fornecidos na base de dados e encripta a password
    @FormUrlEncoded
    @POST("auth/signup/")
    fun  createUser(
        @Field("username") username:String,
        @Field("email") email:String,
        @Field("password") password:String,
    ):retrofit2.Call<RegisterResponse>

    //Mete um código QR na base de dados
    @FormUrlEncoded
    @POST("qrcode/saved")
    fun fetchCodes(
        @Field("messageQR") messageQR: String,
    ):retrofit2.Call<QRCodeResponse>

    //Envia os parâmetros para a API autenticar consoante os dados na base de dados
    @FormUrlEncoded
    @POST("auth/signin/")
    fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String,
    ):retrofit2.Call<LoginResponse>

    //Apaga um código QR consoante o seu ID
    @DELETE("qrcode/{id}")
    fun deleteQRCode(@Path("id") id: String) : retrofit2.Call<Unit>






}