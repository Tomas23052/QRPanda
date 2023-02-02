package com.example.QRPanda.activities


import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.QRPanda.api.RetrofitClient
import com.example.QRPanda.models.LoginResponse
import com.example.QRPanda.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//Atividade onde se fará Login para entrar na aplicação
class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        //Listener do botão para abrir a atividade de registo caso o utilizador não tenha conta
        textViewRegister.setOnClickListener{
            finish()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))

        }

        //Listener do botão de Login para fazer a autenticação do utilizador
        buttonLogin.setOnClickListener{
            val username = editTextUser.text.toString().trim();
            val password = editTextPassword.text.toString().trim();

            //Se o campo do utilizador estiver vazio aparecerá uma mensagem a avisar o utilizador
            if(username.isEmpty()){
                editTextUser.error = "Username é necessário"
                editTextUser.requestFocus()
                return@setOnClickListener
            }

            //Se o campo da palavra-passe estiver vazio aparecerá uma mensagem a avisar o utilizador
            if(password.isEmpty()){
                editTextPassword.error = "Password é necessária"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }




            //Resposta do retrofit usando a função designada na interface da API
            RetrofitClient.instance.userLogin(username, password)
                .enqueue(object: Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        //Se a resposta não for nula, os parâmetros vão ser passados pela API para ver se a autenticação está correta
                        if(response.body() != null) {

                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            //Em seguida esta atividade é fechada e é aberta a próxima
                            finish()
                            val intent = Intent(this@LoginActivity, QRCode::class.java)
                            startActivity(intent)


                        }else{
                            Toast.makeText(
                                applicationContext,
                                "Login inválido",
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    }
                })



        }



    }

}

