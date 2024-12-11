package com.cmj.examen

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cmj.examen.databinding.ActivityCrearCuentaBinding

class CrearCuentaActivity : AppCompatActivity() {
    private var contexto = this
    private lateinit var binding: ActivityCrearCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("usuarios", MODE_PRIVATE)
        val usuariosSP = sharedPreferences.getString("arrayUsuarios", null)
        var arrayUsuarios = mutableListOf<String>()

        if(usuariosSP != null){
            arrayUsuarios = usuariosSP.split(";").toMutableList()
        }

        with(binding){
            val nombre = nombreET.text
            val password = passET.text
            val email = emailET.text

            botonGuardarUsuario.setOnClickListener {
                if(nombre?.isNotEmpty() == true){
                    if(nombre.toString().length <= 16){
                        if(password?.isNotEmpty() == true){
                            val regexNumeros = Regex("[0-9]")
                            val regexMayus = Regex("[A-Z]")

                            if(password.toString().contains(regexNumeros) &&
                                password.toString().contains(regexMayus) &&
                                password.toString().length >= 8){
                                var datosUsuario = "$nombre|$password"

                                var emailExiste = false

                                if(email?.isNotEmpty() == true) {
                                    if(arrayUsuarios[0] != ""){
                                        for(user in arrayUsuarios){
                                            val emailUsuario = user.split("|")[2]

                                            if(emailUsuario.equals(email)){
                                                emailExiste = true
                                                break
                                            }
                                        }

                                    }

                                }

                                if(!emailExiste) datosUsuario += "|$email"
                                else hacerTostada(contexto, "El email ya existe")

                                if(arrayUsuarios[0] != ""){
                                    arrayUsuarios.add(";$datosUsuario")
                                }else arrayUsuarios.add(datosUsuario)

                                val nuevaListaUsuarios = arrayUsuarios.toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(",", "")
                                    .replace(" ", "")

                                sharedPreferences.edit {
                                    putString("arrayUsuarios", nuevaListaUsuarios)
                                    apply()
                                }
                            }else hacerTostada(contexto, "La contraseña debe tener una mayúscula, un número y al menos 8 caracteres")
                        }
                    }else hacerTostada(contexto, "El nombre debe tener como máximo 16 caracteres")
                }
            }
        }
    }

    private fun hacerTostada(contexto: Context, mensaje: String, tiempo: Int = Toast.LENGTH_SHORT){
        Toast.makeText(
            contexto,
            mensaje,
            tiempo
        ).show()
    }
}