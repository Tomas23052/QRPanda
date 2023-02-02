package com.dam.QRPanda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import com.dam.QRPanda.api.RetrofitClient
import com.dam.QRPanda.models.RegisterResponse
import com.dam.QRPanda.R
import kotlinx.android.synthetic.main.layout_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        //Listener para o botão caso o utilizador já tenha conta, assim abre a atividade de Login
        buttonRegister.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))

        }

        //Listener para o botão de registo
        buttonSignUp.setOnClickListener(){
            val username = editTextUser.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            //Se algum dos campos estiver vazio o utilizador é avisado
            if(username.isEmpty()){
                editTextUser.error = "O username é necessário"
                editTextUser.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()){
                editTextEmail.error = "Email é necessário"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                editTextPassword.error = "Password é necessária"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            //Verificação do email
            if(!isValidEmail(email)){
                editTextEmail.error = "Email inválido"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            //Resposta do retrofit usando a funcção designada na interface da API
            RetrofitClient.instance.createUser(username, email, password)
                .enqueue(object: Callback<RegisterResponse>{
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Ocorreu um erro", Toast.LENGTH_SHORT).show()

                    }
                    //Resposta que manda os parâmetros para a base de dados
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if(response.body() == null){
                            //Se já existir um utilizador com o mesmo nome ou email o utilizador é avisado
                            Toast.makeText(this@MainActivity, "Utilizador ou Email já está em uso!", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@MainActivity, response.body()?.message.toString().trim(), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        }





                    }

                })
        }
    }

    //Função que valida o email inserido
    private fun isValidEmail(email: String): Boolean{
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}
