package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.todoapp.Database.UserCallback


class MainActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login);
        loadActions();

    }

    /**
     * Funcion para obtener los elementos del layout
     * */
    private fun loadActions() {
        // seleccionando los elementos del layout
        val editEmailText:EditText = findViewById(R.id.textEmail);
        val editPasswordText: EditText = findViewById(R.id.textPassword);
        val btnLogin: Button = findViewById(R.id.btnLogin);
        val linkRegister: TextView = findViewById(R.id.link_register);

        var isFirstFocusEmail = true;
        var isFirstFocusPassword = true;

        // limpiar el campo email cuando tiene el focus
        editEmailText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && isFirstFocusEmail) {
                editEmailText.text.clear()
                isFirstFocusEmail = false
            }
        }

        editPasswordText.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && isFirstFocusPassword) {
                editPasswordText.text.clear();
                isFirstFocusPassword = false;
            }
        }

        linkRegister.setOnClickListener  {
            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent);
        }

        getDataLogin( btnLogin, editEmailText, editPasswordText );
    }




    /**
     * Funcion para capturar los datos del formulario del login
     * @property btnLogin button para capturar los datos del usuario
     * @property emailText campo de texto para obtener el email
     * @property passwordText campo de texto para obtener la contraseña
     * */
    private fun getDataLogin ( btnLogin:Button, emailText:EditText, passwordText:EditText ){
        btnLogin.setOnClickListener {
            var email = emailText.text.toString();
            var password = passwordText.text.toString();

            if(!isValidEmail(email)) {
                emailText.error = "Correo invalido";
                Toast.makeText(this, "formato de correo invalido", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            if ( !isValidPassword(password) ) {
                passwordText.error = "La contraseña debe contener mas de 8 caracteres";
                Toast.makeText(this, "La contraseña debe contener mas de 8 caracteres", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            // redireccionamiento a home
            val database = Database();

            database.get_user("users", email,  UserCallback { user ->
                if (user == null || !user.email.equals(email) || !user.password.equals(password)){
                    Toast.makeText(this, "El correo y/o contraseaña son invalidos", Toast.LENGTH_LONG).show();
                    return@UserCallback;
                }


                var intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("user_email", user.email);
                };
                startActivity(intent);
            });
        }
    }

    /**
     * funcion para validar el campo de texto del email
     * @property email dato del campo email
     */
    private fun isValidEmail ( email:String ): Boolean{
        return  email.isNotBlank() && email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * funcion para validar el campo password
     * @property password dato del campo passwaord
     */
    private fun isValidPassword ( password:String ): Boolean {
        return password.isNotBlank() && password.isNotEmpty() && password.length >= 8;
    }


}

