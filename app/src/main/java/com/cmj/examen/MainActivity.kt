package com.cmj.examen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cmj.examen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var contexto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
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

        println(usuariosSP)
        println(arrayUsuarios.toString())

        with(binding){
            val usuario = userET.text
            val password = passET.text

            botonSubmit.setOnClickListener {
                if(usuario?.isNotEmpty() == true){
                    if(password?.isNotEmpty() == true){
                        for(user in arrayUsuarios){
                            val datosUsuario = user.split("|")

                            if(datosUsuario[0] == usuario.toString() && datosUsuario[1] == password.toString()){
                                hacerTostada(contexto, "Inicio de sesión correcto")

                                val intent = Intent(contexto, ListadoUsuariosActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }else hacerTostada(contexto, "La contraseña está vacío")
                }else {
                    hacerTostada(contexto, "El usuario está vacío")

//                    sharedPreferences.edit {
//                        putString("arrayUsuarios", "")
//                        apply()
//                    }
                }
            }

            botonCrearCuenta.setOnClickListener {
                val intent = Intent(contexto, CrearCuentaActivity::class.java)
                startActivity(intent)
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