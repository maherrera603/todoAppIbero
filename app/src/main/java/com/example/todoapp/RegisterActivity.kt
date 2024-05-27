package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register2);

        // elementos del layout
        val name: EditText = findViewById(R.id.name);
        val lastName: EditText = findViewById(R.id.lastname);
        val phone: EditText = findViewById(R.id.phone);
        val email: EditText = findViewById(R.id.email);
        val password: EditText = findViewById(R.id.password);
        val newPassword: EditText = findViewById(R.id.newPassword);
        val btnRegister: Button = findViewById(R.id.btnRegister);
        val linkLogin: TextView = findViewById(R.id.link_login);

        // clear text
        clearFieldText( name );
        clearFieldText( lastName );
        clearFieldText( phone );
        clearFieldText( email );
        clearFieldText( password );
        clearFieldText( newPassword );

        // captura de datos
        btnRegister.setOnClickListener {
            getDataRegister(name, lastName, phone, email, password, newPassword);
        }

        linkLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }

    }

    /**
     * Funcion para capturar, validar y guardar los datos
     * del usuario para registrarlos
     * @property name campo de texto para nombre
     * @property lastName campo de texto para apellido
     * @property phone campo de texto para telefono
     * @property email campo de texto para correo
     * @property password campo de texto para contraseña
     * @property newPassword campo de texto para la contraseña
     */
    private fun getDataRegister (name:EditText, lastName:EditText, phone: EditText, email: EditText, password:EditText, newPassword:EditText) {
        // validar los campos de textos
        if( isValidText( name ) ) {
            name.error = "Ingrese el nombre completo";
            return ;
        }

        if ( isValidText( lastName ) ) {
            lastName.error = "Ingrese los apellidos completos";
            return ;
        }

        if ( isValidPhone( phone ) ) {
            phone.error = "Ingrese un numero de telefono valido";
            return ;
        }

        if (isValidEmail( email ) ) {
            email.error = "Ingrese un correo valido";
            return ;
        }

        if ( isValidPassword( password ) ) {
            password.error = "Ingrese una contraseña que contenga mas de 8 caracteres";
            return ;
        }

        if ( isValidPassword( newPassword ) ) {
            newPassword.error = "Ingrese una contraseña que contenga mas de 8 caracteres";
            return ;
        }

        if ( !isEqualPassword( password, newPassword ) ) {
            newPassword.error = "Las contraseñas no coinciden";
            return ;
        }


        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }


    /**
     * Funcion para limpiar un campo de texto
     * @property field parametro de tipo EditText
     */
    private fun clearFieldText( field:EditText ) {
        var isFirstFocusField = true;
        field.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && isFirstFocusField) {
                field.text.clear();
                isFirstFocusField = false;
            }
        }
    }

    /**
     * Funcion para validar los campos de texto name, lastname
     * @property  field parametro de tipo EditText
     * @return un valor booleano
     */
    private fun isValidText( field:EditText ): Boolean {
        val data = field.text.toString();
        return data.length <= 2 || data.isBlank() || data.isEmpty()
    }

    /**
     * Funcion para validar el capo telefono
     * @property  field parametro de tipo EditText
     * @return un valor booleano
     */
    private fun isValidPhone( field:EditText ): Boolean {
        val data = field.text.toString();
        return data.length < 10 || data.length > 10
    }

    /**
     * Funcion para validar el capo email
     * @property  field parametro de tipo EditText
     * @return un valor booleano
     */
    private fun isValidEmail( field:EditText ): Boolean {
        val data = field.text.toString();
        return data.isBlank() || data.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(data).matches();
    }

    /**
     * Funcion para validar el capo password
     * @property  field parametro de tipo EditText
     * @return un valor booleano
     */
    private fun isValidPassword( field:EditText ): Boolean {
        val data = field.text.toString();
        return data.length < 8 ;
    }

    /**
     * funcion para validar si las contraseña son iguales
     * @property password parametro de tipo EditText
     * @property newPassword parametro de tipo EditText
     * @return retorna un valor booelano
     */
    private fun isEqualPassword(password: EditText, newPassword: EditText): Boolean {
        return newPassword.text.toString() == password.text.toString();
    }



}