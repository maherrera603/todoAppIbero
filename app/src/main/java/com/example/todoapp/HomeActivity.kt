package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.todoapp.Database.TaskCallback
import com.example.todoapp.Database.ResultCallback
import com.example.todoapp.Database.TasksCallback
import com.example.todoapp.models.TaskTodo
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // datos del usuario
        val user_email = intent.getStringExtra("user_email").toString();



        // elementos del view
        val btnClose: Button = findViewById(R.id.btn_close);
        val searchText: EditText = findViewById(R.id.searchText);
        val btnSearch: Button = findViewById(R.id.btnSearch);
        val btnAdd: Button = findViewById(R.id.btnAdd);
        val listTask = findViewById<TextView>(R.id.listTask);
        // limpiamos el texto
        this.clearFieldText(searchText);

        // volver a la pantalla principal
        btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }

        // buscar tareas
        btnSearch.setOnClickListener {
            this.getTask(searchText, user_email, listTask);
        }

        // agregar tareas
        btnAdd.setOnClickListener {
            this.openPopupView(user_email, listTask);
        }




        this.allTasks(listTask, user_email);
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
    private fun getTask(search:EditText, email: String, listTask: TextView) {
        val task = search.text.toString();

        val database = Database();
        database.get_task("tasks", task, email, Database.TaskCallback { task ->
            if(task == null) {
                Toast.makeText(this, "Tarea '${task}' no encontrada", Toast.LENGTH_LONG).show();
                return@TaskCallback
            }
            val tasksStringBuilder = StringBuilder()
            tasksStringBuilder.append(task.title).append("\n");
            listTask.text = tasksStringBuilder.toString()
            // Toast.makeText(this, "Tarea '${task.toString()}' no encontrada", Toast.LENGTH_LONG).show();
            return@TaskCallback
        })
    }

    /**
     * Funcion para abrir el popup para agregar
     * la nueva tarea
     * @property email correo electronico del usuario
     */
    private fun openPopupView(email:String, listTask: TextView) {
        try {
            val builder = MaterialAlertDialogBuilder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.form_layout, null);
            builder.setView(dialogLayout);

            val dialog = builder.create()
            dialog.show()

            // obtenemos los campos
            val dateText = dialogLayout.findViewById<EditText>(R.id.TextDate);
            val titleTask = dialogLayout.findViewById<EditText>(R.id.titleTask);
            val textDescription = dialogLayout.findViewById<EditText>(R.id.textDescription);
            val btnAdd = dialogLayout.findViewById<Button>(R.id.btnAdd);

            // limpiamos los campos
            this.clearFieldText(dateText);
            this.clearFieldText(titleTask);
            this.clearFieldText(textDescription);

            // ejecutamos accion click
            btnAdd.setOnClickListener {
                val task = TaskTodo(dateText.text.toString(), titleTask.text.toString(), textDescription.text.toString(), email);
                Toast.makeText(this, "${task.description}", Toast.LENGTH_LONG).show();
                val database = Database();
                database.create_task("tasks", task, ResultCallback { result ->
                    if(!result) {
                        Toast.makeText(this, "Tarea no guardada", Toast.LENGTH_LONG).show();
                        return@ResultCallback
                    }

                    Toast.makeText(this, "Tarea guardada", Toast.LENGTH_LONG).show();

                    // cerramos la ventana
                    dialog.dismiss();
                });
                this.allTasks(listTask, email);
            }

        }catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show();
        }

    }


    private fun allTasks(listTask:TextView, user_email:String) {
        val database = Database()
        database.allTasks("tasks", user_email, TasksCallback { tasks ->
            val tasksStringBuilder = StringBuilder()
            tasks.forEach { task ->

                tasksStringBuilder.append(task).append("\n")
            }
            runOnUiThread {
                listTask.text = tasksStringBuilder.toString()
            }
        });
    }
}