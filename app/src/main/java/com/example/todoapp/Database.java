package com.example.todoapp;


import androidx.annotation.NonNull;
import android.util.Log;

import com.example.todoapp.models.TaskTodo;
import com.example.todoapp.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User newUser = new User();

    public Database() {}

    public void create_user( String collection, User user, final ResultCallback callback ) {
        this.db.collection(collection)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Database", "User added with ID: " + documentReference.getId());
                        callback.onResult(true);  // Return true on success
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Database", "Error adding user", e);
                        callback.onResult(false);  // Return false on failure
                    }
                });
    }

    public void get_user( String collection, String email, final UserCallback callback ) {
        this.db.collection(collection)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = null; // Declare a new user variable
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = document.toObject(User.class);
                                break; // Assume we only need the first match
                            }
                            callback.onCallback(user);
                        } else {
                            Log.w("Database", "Error getting documents.", task.getException());
                            callback.onCallback(null);
                        }
                    }
                });
    }

    public void create_task( String collection, TaskTodo task, final ResultCallback callback ) {
        this.db.collection( collection )
                .add( task )
                .addOnSuccessListener( new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess( DocumentReference documentReference ) {
                        callback.onResult(true);  // Return true on success
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( @NonNull Exception e ) {
                        callback.onResult(false);  // Return false on failure
                    }
                });
    }

    public void get_task( String collection, String task, String email, final TaskCallback callback ) {
        this.db.collection(collection)
                .whereEqualTo("email", email)
                .whereEqualTo("title", task)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            TaskTodo taskTodo = null; // Declare a new user variable
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                taskTodo = document.toObject(TaskTodo.class);
                                break; // Assume we only need the first match
                            }
                            callback.onCallback(taskTodo);
                        } else {
                            Log.w("Database", "Error getting documents.", task.getException());
                            callback.onCallback(null);
                        }
                    }
                });
    }


    public void allTasks(String collection, String email, TasksCallback callback) {
        this.db.collection(collection)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> tasks = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String taskName = document.getString("title"); // Asumiendo que el campo se llama 'taskName'
                        if (taskName != null) {
                            tasks.add(taskName);
                        }
                    }
                    callback.onCallback(tasks);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error getting documents: ", e);
                    callback.onCallback(new ArrayList<>()); // Retorna una lista vacía en caso de error
                });
    }



    public interface UserCallback {
        void onCallback(User user);
    }

    public interface ResultCallback {
        void onResult(boolean success);
    }

    public interface TaskCallback {
        void onCallback(TaskTodo task);
    }

    public interface TasksCallback {
        void onCallback(List<String> tasks);
    }
}
