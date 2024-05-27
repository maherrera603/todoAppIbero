package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // elementos del view
        val btnClose: Button = findViewById(R.id.btn_close);
        val searchText: EditText = findViewById(R.id.searchText);
        val btnSearch: Button = findViewById(R.id.btnSearch);
        val btnAdd: Button = findViewById(R.id.btnAdd);

        // limpiamos el texto
        this.clearFieldText(searchText);

        // volver a la pantalla principal
        btnClose.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }

        // buscar tareas
        btnSearch.setOnClickListener {
            this.getTask(searchText);
        }

        // agregar tareas
        btnAdd.setOnClickListener {
            this.openPopupView();
        }
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
     * Funcion para realizar la busqueda de tareas
     * @property search campo de texto de busqueda
     */
    private fun getTask(search:EditText) {
        var task = search.text.toString();
        Toast.makeText(this, "Tarea '${task}' no encontrada", Toast.LENGTH_LONG).show();
    }

    /**
     * Funcion para abrir el popup para agregar
     * la nueva tarea
     */
    private fun openPopupView() {
        Toast.makeText(this, "Accion para abrir el popup de crear la tarea", Toast.LENGTH_LONG).show();
    }

}