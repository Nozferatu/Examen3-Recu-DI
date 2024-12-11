package com.cmj.examen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cmj.examen.databinding.ActivityListadoUsuariosBinding

class ListadoUsuariosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListadoUsuariosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListadoUsuariosBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("usuarios", MODE_PRIVATE)
        val usuariosSP = sharedPreferences.getString("arrayUsuarios", "")
        var arrayUsuarios = mutableListOf<String>()

        if(usuariosSP != null){
            arrayUsuarios = usuariosSP.split(";").toMutableList()
        }

        with(binding){
            //Muestro los usuarios ya existentes
            var listado = ""
            val emailInput = emailET.text

            for(user in arrayUsuarios){
                val datosUsuario = user.split("|")
                val nombre = datosUsuario[0]
                val email = datosUsuario[2]

                listado += "$nombre - $email\n"
            }

            listadoUsuarios.text = listado

            botonBorrar.setOnClickListener {
                if(emailInput?.isNotEmpty() == true){
                    var existe = false
                    var datosUsuario: List<String>
                    var nombre= ""
                    var password = ""
                    var email = ""

                    for(user in arrayUsuarios){
                        datosUsuario = user.split("|")
                        nombre = datosUsuario[0]
                        password = datosUsuario[1]
                        email = datosUsuario[2]

                        if(email.equals(emailInput)){
                            existe = true
                            break
                        }
                    }

                    if(existe){
                        arrayUsuarios.remove("$nombre|$password|$email")

                        val nuevaListaUsuarios = arrayUsuarios.toString()
                            .replace("[", "")
                            .replace("]", "")
                            .replace(",", "")
                            .replace(" ", "")

                        sharedPreferences.edit {
                            putString("arrayUsuarios", nuevaListaUsuarios)
                            commit()
                        }

                        recreate()
                    }
                }
            }
        }
    }
}